package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable


@Serializable
data class ScrapDataRequest(
    val domainName: String,
    val url: String
)
