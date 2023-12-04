package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CompleteProfileRequest(
    val avatarImageName: String,
    val color: String,
    val faceStyle: String,
    val gender: String,
    val hairColor: String,
    val hairStyle: String,
    val nickname: String,
    val userId: Int
)