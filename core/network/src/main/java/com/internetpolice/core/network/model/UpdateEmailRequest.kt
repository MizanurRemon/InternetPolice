package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable


@Serializable
data class UpdateEmailRequest(

    val newEmail: String,
    //val confirmEmail: String,
    val userId: Int

)
