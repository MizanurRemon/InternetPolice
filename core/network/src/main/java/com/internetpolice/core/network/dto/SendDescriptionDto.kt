package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable


@Serializable
data class SendDescriptionDto(
    var statusCode: String,
    var message: String
)
