package com.internetpolice.auth.auth_domain.model

data class LoginModel(
    val email: String,
    val password: String,
)