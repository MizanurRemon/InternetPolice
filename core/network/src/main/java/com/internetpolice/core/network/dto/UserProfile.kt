package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Int = 0,
    val ageCategory: String?=null,
    val avatarImageName: String? =null,
    val avatarImagePath: String? =null,
    val color: String? =null,
    val faceStyle: String? =null,
    val gender: String? =null,
    val hairColor: String? =null,
    val hairStyle: String? =null,
    val isProfileCompleted: Boolean=false,
    val language: String? =null,
    val nickname: String? =null,
    val rank: String?=null
)