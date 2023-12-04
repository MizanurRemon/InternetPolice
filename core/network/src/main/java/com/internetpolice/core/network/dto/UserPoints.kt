package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserPoints(
    val totalPoints: Int,
    val userPointList: List<UserPoint>?
)