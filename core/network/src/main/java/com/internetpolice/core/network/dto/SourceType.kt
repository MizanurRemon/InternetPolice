package com.internetpolice.core.network.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class SourceType(
    val description: String?=null,
    val displayName: String?=null,
    val id: Int=0,
    val typeKey: String?=null
):Parcelable