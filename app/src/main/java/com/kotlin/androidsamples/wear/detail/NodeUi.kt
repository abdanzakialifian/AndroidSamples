package com.kotlin.androidsamples.wear.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class NodeUi(
    val id: String,
    val displayName: String,
    val isNearby: Boolean,
) : Parcelable