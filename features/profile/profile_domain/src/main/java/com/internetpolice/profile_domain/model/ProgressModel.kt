package com.internetpolice.profile_domain.model

data class ProgressModel(
    val points: List<UserPointModel>,
    val votes: List<VoteModel>,
)
