package com.internetpolice.auth.auth_domain.model

data class UpdateEmailModel(
    val newEmail: String,
    val userId : Int

)
