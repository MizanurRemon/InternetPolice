package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportResponse(
    val allowedVoteSubmit: Boolean,
    val category: String,
    val domainStoreId: Int,
    val trustTemplateId: Int,
    val voteStatus: String,
    val voteType: String,
    val voterId: Int
)