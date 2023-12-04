package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable


@Serializable
data class BasicInfoRequest(
    val ageCategory: String,
    val nickname: String
)
