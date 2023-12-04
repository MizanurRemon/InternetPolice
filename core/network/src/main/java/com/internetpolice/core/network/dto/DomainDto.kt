package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class DomainDto(
    val domain: String,
    val id: Int?,
    val negativeResultCount: Int,
    val trustScore: Double,
    val voteCount: Int
)