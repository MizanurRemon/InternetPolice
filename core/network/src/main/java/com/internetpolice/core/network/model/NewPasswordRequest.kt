package com.internetpolice.core.network.model

data class NewPasswordRequest(
    val confirmPassword: String,
    val password: String
)