package com.internetpolice.profile_domain.model

data class CompleteProfileModel(
    val avatarImagePath: String,
    val id: Int,
    val isProfileCompleted: Boolean,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val totalPoints: Int=0,

)