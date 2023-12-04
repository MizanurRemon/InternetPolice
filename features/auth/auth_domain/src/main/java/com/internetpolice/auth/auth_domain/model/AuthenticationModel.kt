package com.internetpolice.auth.auth_domain.model

data class AuthenticationModel(
    var username: String,
    var password: String,
    var rememberMe: Boolean = false,
    var deviceId: String = ""
)
