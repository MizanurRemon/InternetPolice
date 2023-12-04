package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val ageCategory: String,
    val confirmPassword: String,
    val deviceId: String?=null,
    val email: String,
    val isAgreedToTermsAndConditions: Boolean=true,
    val language: String,
    val password: String,
    val rememberMe: Boolean=true
)