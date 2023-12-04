package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportRequest(
    val category: String,
    val domainStoreId: Int,
    val trustTemplateId: Int,
    val voteStatus: String,
    val voteType: String,
    val voterId: Int,
    val description: String
)