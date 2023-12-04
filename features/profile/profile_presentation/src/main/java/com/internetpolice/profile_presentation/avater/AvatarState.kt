package com.internetpolice.profile_presentation.avater

import com.internetpolice.database.entity.UserEntity


data class AvatarState(
    val name: String = "",
    val url: String = "",
    val isShowLoading: Boolean = false,
    val isError: Boolean = false,
    val totalPoints: Int = 0,
    val userEntity: UserEntity? = null
)
