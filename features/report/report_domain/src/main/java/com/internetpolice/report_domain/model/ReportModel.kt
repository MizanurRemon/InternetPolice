package com.internetpolice.report_domain.model

data class ReportModel(
    val category: String,
    val domainStoreId: Int,
    val domainName:String,
    val trustTemplateId: Int,
    val voteStatus: String,
    val voteType: String,
    val description: String?,
)