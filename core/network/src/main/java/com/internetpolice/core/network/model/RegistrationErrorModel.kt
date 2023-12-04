package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationErrorModel(
    val email: List<String>? = null,
    val username: List<String>? = null,
    val timestamp: Long = 0,
    val status: Int = 0,
    val error: String? = null,
    val message: String? = null,
    val statusCode: Int = 0
)
