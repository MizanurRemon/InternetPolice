package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    val access_token: String,
    val refresh_token: String,
    val userId: Int,
    val userProfile: UserProfile
)