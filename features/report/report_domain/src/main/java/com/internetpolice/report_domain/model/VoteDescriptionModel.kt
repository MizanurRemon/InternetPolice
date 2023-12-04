package com.internetpolice.report_domain.model

data class VoteDescriptionModel(
    val avatarImagePath: String,
    val category: String,
    val description: String,
    val id: Int,
    val nickname: String,
    val rank: String,
    val voteStatus: String,
    val voteType: String,
)