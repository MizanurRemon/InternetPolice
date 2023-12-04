package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class Vote(
    val category: String,
    val createdDate: Long,
    val description: String,
    val domainName: String,
    val id: Int,
    val voteStatus: String,
    val voteType: String
)