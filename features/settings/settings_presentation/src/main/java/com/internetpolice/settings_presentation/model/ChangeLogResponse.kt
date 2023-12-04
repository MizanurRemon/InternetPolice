package com.internetpolice.settings_presentation.model

data class ChangeLogResponse(var msg: String)
data class ChangeLogDetailsListResponse(
    val version: String,
    val changeLogResponseList: List<ChangeLogResponse> = emptyList()
)
