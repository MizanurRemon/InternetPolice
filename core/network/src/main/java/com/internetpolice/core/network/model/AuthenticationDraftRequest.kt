package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable


@Serializable
data class AuthenticationDraftRequest(
    var username: String,
    var password: String,
    var rememberMe: Boolean,
    var deviceId: String
)
