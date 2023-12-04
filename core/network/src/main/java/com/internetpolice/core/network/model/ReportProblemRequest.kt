package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportProblemRequest(
    val description: String,
    val userId: Int,
    val issueCategory : String
)