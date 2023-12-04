package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDataUpdateRequest(
    val nickname: String,
    val userId: Int,
    val ageCategory:String
)