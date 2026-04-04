package com.android.playground.device

import android.os.Build
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class DeviceInfo(
    val manufacturer: String = Build.MANUFACTURER,
    val model: String = Build.MODEL,
    val brand: String = Build.BRAND,
    val device: String = Build.DEVICE,
    val product: String = Build.PRODUCT,
    val sdkVersion: Int = Build.VERSION.SDK_INT,
): Parcelable {
    fun encodeToByteArray(): ByteArray {
        val json = Json {
            encodeDefaults = true
        }
        return json.encodeToString(this).toByteArray()
    }
}


