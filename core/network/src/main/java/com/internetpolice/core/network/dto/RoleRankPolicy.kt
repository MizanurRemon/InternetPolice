package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RoleRankPolicy(
    val roleId: Int,
    val xpRangeFrom: Int,
    val xpRangeTo: Int
)