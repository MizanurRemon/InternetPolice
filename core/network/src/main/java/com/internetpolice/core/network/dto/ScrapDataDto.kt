package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable


@Serializable
data class ScrapDataDto(
    val sellerName: String? = null,
    val exist: Boolean = false
)
