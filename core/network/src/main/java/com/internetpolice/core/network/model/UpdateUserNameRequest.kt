package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserNameRequest(
    val nickname: String,
    val userId: Int
)