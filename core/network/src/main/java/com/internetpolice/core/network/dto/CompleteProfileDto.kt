package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompleteProfileDto(
    val ageCategory: String,
    val avatarImageName: String,
    val avatarImagePath: String,
    val color: String,
    val faceStyle: String,
    val gender: String,
    val hairColor: String,
    val hairStyle: String,
    val id: Int,
    val isProfileCompleted: Boolean,
    val language: String,
    val nickname: String,
    val rank: String,
    val userId: Int
)