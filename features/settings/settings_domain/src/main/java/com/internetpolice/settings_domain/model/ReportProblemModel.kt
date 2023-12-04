package com.internetpolice.settings_domain.model

data class ReportProblemModel(
    val id: Int,
    val userId: Int,
    val description: String,
)