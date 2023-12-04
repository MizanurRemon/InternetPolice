package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable


@Serializable
data class UserWaitingRequest(
    var email: String
)
