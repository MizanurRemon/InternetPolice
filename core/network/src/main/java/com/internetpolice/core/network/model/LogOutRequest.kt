package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class LogOutRequest(
    val refresh_token: String
)