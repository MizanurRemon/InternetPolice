package com.internetpolice.core.network.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class DomainDetailsDto(
    val browserPackageName: String? = null,
    val capturedUrl: String? = null,
    val domain: String? = null,
    val id: Int = 0,
    val reviewLock: Boolean = false,
    val sourceType: SourceType? = null,
    val trustScore: Double = 0.0,
    val isECommerceScrapEnabled: Boolean? = false
) : Parcelable