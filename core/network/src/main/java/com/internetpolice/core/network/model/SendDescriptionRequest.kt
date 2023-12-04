package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable


@Serializable
data class SendDescriptionRequest(
    var nickname: String?,
    var email: String,
    var description: String,
    var language: String
)
