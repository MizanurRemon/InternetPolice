package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResendVerificationRequest(
    val email: String,
    val username: String,
    val forProfileUpdate: Boolean
)