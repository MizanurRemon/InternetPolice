package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportProblemDto(
    val description: String,
    val id: Int,
    val userId: Int,

    )