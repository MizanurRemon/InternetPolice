package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class  DomainNameDetailsDto(
    val resultMessageKeys: List<String>,
    val totalVotes: Int,
    val voteList: List<VoteDescription>,
)