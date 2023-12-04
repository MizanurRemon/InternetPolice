package com.internetpolice.report_domain.model
data class ReportResponseModel(
    val allowedVoteSubmit: Boolean,
    val category: String,
    val domainStoreId: Int,
    val trustTemplateId: Int,
    val voteStatus: String,
    val voteType: String,
    val voterId: Int
)