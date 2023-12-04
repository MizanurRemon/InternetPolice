package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class WebPageDto(
    var id: Int,
    var content: WebPageContent,

    )

@Serializable
data class WebPageContent(val rendered: String)
