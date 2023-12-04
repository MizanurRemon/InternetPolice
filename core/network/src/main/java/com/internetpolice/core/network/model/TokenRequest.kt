package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val token: String,
)