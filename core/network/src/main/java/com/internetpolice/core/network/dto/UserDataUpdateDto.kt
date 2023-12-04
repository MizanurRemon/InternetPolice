package com.internetpolice.core.network.dto
import kotlinx.serialization.Serializable

@Serializable
data class UserDataUpdateDto(
    val id: Int,
    val nickname: String,
    val userId: Int,
    val ageCategory: String,

)