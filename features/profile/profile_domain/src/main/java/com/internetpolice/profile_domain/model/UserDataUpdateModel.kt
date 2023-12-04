package com.internetpolice.profile_domain.model

data class UserDataUpdateModel(
    val id: Int,
    val nickname: String,
    val ageCategory: String,
    val userId: Int
)