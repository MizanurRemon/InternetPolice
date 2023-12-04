package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class VoteListDto(
    val totalVote: Int,
    val voteList: List<Vote>
)