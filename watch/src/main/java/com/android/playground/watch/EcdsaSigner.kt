package com.android.playground.watch

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.StrongBoxUnavailableException
import android.util.Base64
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.ECGenParameterSpec
import java.security.spec.X509EncodedKeySpec

/**
 * Utility object for handling ECDSA (P-256) digital signature operations
 * using Android Keystore system.
 *
 * This implementation:
 * - Generates and stores the EC key pair inside Android Keystore
 * - Keeps private key non-exportable
 * - Supports signing using SHA256withECDSA
 * - Allows verification using an external public key
 *
 * Curve: secp256r1 (NIST P-256)
 * Signature Algorithm: SHA256withECDSA
 *
 * ⚠️ Private key never leaves the Android Keystore.
 */
object EcdsaSigner {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

    private const val EC_CURVE_P256 = "secp256r1"

    private const val SIGNATURE_ALGORITHM = "SHA256withECDSA"

    object KeyAlias {
        const val ECDSA = "ecdsa_key"
    }

    /**
     * Generates an ECDSA (P-256) key pair inside Android Keystore if it does not already exist.
     *
     * Key characteristics:
     * - Algorithm      : EC
     * - Curve          : secp256r1 (NIST P-256)
     * - Purpose        : SIGN and VERIFY
     * - Digest         : SHA-256
     * - Storage        : AndroidKeyStore
     *
     * StrongBox behavior:
     * This method first attempts to generate the key inside StrongBox (hardware-backed secure element).
     * If StrongBox is unavailable or does not support the requested configuration,
     * it automatically falls back to standard hardware-backed Keystore (TEE).
     *
     * Notes:
     * - Private key is non-exportable.
     * - Safe to call multiple times (idempotent).
     * - If alias already exists, key generation will be skipped.
     *
     * @throws RuntimeException if key generation fails for unexpected reasons.
     */
    fun createKeyIfNeeded(keyAlias: String) {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore?.load(null)

        if (keyStore?.containsAlias(keyAlias) == true) return

        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            ANDROID_KEYSTORE
        )

        try {
            keyPairGenerator?.initialize(buildSpec(isStrongBoxBacked = true, keyAlias = keyAlias))
            keyPairGenerator?.generateKeyPair()
        } catch (_: StrongBoxUnavailableException) {
            keyPairGenerator?.initialize(buildSpec(isStrongBoxBacked = false, keyAlias))
            keyPairGenerator?.generateKeyPair()
        }
    }

    /**
     * Builds a [KeyGenParameterSpec] for ECDSA P-256 key generation.
     *
     * @param isStrongBoxBacked Indicates whether the key should be stored in StrongBox
     *                          secure hardware (if available).
     *
     * When true:
     * - The system will attempt to store the key inside StrongBox.
     * - May throw [StrongBoxUnavailableException] during initialization or generation
     *   if StrongBox does not support the configuration.
     *
     * When false:
     * - The key will be stored in standard hardware-backed Keystore (TEE) if available,
     *   otherwise software-backed.
     *
     * @return Configured [KeyGenParameterSpec] for EC signing and verification.
     */
    private fun buildSpec(isStrongBoxBacked: Boolean, keyAlias: String): KeyGenParameterSpec {
        val builder = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        ).run {
            if (isStrongBoxBacked) {
                setIsStrongBoxBacked(true)
            }
            setAlgorithmParameterSpec(ECGenParameterSpec(EC_CURVE_P256))
            setDigests(KeyProperties.DIGEST_SHA256)
        }
        return builder.build()
    }

    /**
     * Signs the provided data using the private key stored in Android Keystore.
     *
     * @param data String data to be signed
     * @return Base64 encoded signature, or null if signing fails
     *
     * Flow:
     * 1. Retrieve private key from Keystore
     * 2. Initialize Signature with SHA256withECDSA
     * 3. Feed data
     * 4. Generate signature
     */
    fun sign(data: String?, keyAlias: String): String? {
        return try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore?.load(null)

            val privateKey = keyStore?.getKey(keyAlias, null) as? PrivateKey ?: return null

            val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
            signature?.initSign(privateKey)
            signature?.update(data?.toByteArray() ?: byteArrayOf())
            val signBytes = signature?.sign()
            Base64.encodeToString(signBytes, Base64.NO_WRAP)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Verifies a signature using a provided public key.
     *
     * @param data Original data that was signed
     * @param signatureBase64 Base64 encoded signature
     * @param publicKeyBase64 Base64 encoded public key (X.509 format)
     *
     * @return true if signature is valid, false if invalid, null if error occurs
     *
     * This method does NOT use the private key.
     * It reconstructs the PublicKey object from Base64.
     */
    fun verify(data: String?, signatureBase64: String?, publicKeyBase64: String?): Boolean? {
        return try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore?.load(null)

            val publicKey = parseToPublicKeyFromBase64(publicKeyBase64 ?: "")

            val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
            signature?.initVerify(publicKey)
            signature?.update(data?.toByteArray() ?: byteArrayOf())

            val signatureBytes = Base64.decode(signatureBase64, Base64.NO_WRAP) ?: byteArrayOf()
            signature?.verify(signatureBytes)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Retrieves the stored public key from Android Keystore.
     *
     * @return Base64 encoded public key (X.509 encoded format)
     *
     * This public key can be:
     * - Sent to backend
     * - Stored remotely
     * - Used for verification on another device/server
     */
    fun getPublicKeyBase64(keyAlias: String): String? {
        return try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore?.load(null)

            val publicKey = keyStore?.getCertificate(keyAlias)?.publicKey ?: return null
            Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Converts Base64 encoded public key into a PublicKey object.
     *
     * @param publicKeyBase64 Base64 encoded X.509 public key
     * @return PublicKey instance or null if invalid
     *
     * Used when:
     * - Verifying signature using externally provided public key
     * - Validating server-issued keys
     */
    fun parseToPublicKeyFromBase64(publicKeyBase64: String?): PublicKey? {
        return try {
            val keyBytes = Base64.decode(publicKeyBase64 ?: "", Base64.NO_WRAP)
            val keySpec = X509EncodedKeySpec(keyBytes ?: byteArrayOf())
            val keyFactory = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_EC)
            keyFactory?.generatePublic(keySpec)
        } catch (_: Exception) {
            null
        }
    }
}