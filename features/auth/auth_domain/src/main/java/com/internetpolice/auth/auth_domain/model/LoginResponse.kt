package com.internetpolice.auth.auth_domain.model

import com.internetpolice.core.network.dto.UserProfile

data class LoginResponse(
    val access_token: String,
    val refresh_token: String,
    val userId: Int,
    val userProfile: UserProfile
)