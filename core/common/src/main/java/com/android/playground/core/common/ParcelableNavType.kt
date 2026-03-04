package com.android.playground.core.common

import android.net.Uri
import android.os.Build
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

inline fun <reified T: Parcelable> createNavType(
    serializer: KSerializer<T>
) = ParcelableNavType(T::class.java, serializer)

class ParcelableNavType<T : Parcelable>(
    private val clazz: Class<T>,
    private val serializer: KSerializer<T>,
    override val isNullableAllowed: Boolean = false,
) : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: SavedState, key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, clazz)
        } else {
            bundle.getParcelable(key) as? T
        }
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(serializer, value)
    }

    override fun serializeAsValue(value: T): String {
        return Uri.encode(Json.encodeToString(serializer, value))
    }
}