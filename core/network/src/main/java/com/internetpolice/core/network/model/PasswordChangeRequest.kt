package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PasswordChangeRequest(
    val password: String,
    val newPassword: String,
    val newConfirmPassword: String,
    val id:Int

)