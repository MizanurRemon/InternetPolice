package com.internetpolice.report_domain.model

data class DomainNameDetailsModel(
    val resultMessageKeys: List<String>,
    val totalVotes: Int,
    val voteList: List<VoteDescriptionModel>,
)