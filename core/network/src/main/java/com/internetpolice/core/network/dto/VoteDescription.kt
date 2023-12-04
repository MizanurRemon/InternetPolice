package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class VoteDescription(
    val avatarImagePath: String?,
    val category: String?,
    val description: String?,
    val id: Int,
    val nickname: String,
    val rank: String?,
    val voteStatus: String?,
    val voteType: String?,
)