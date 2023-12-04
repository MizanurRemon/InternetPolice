package com.internetpolice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class DraftVerificationRequest(
    var userId: String,
    var username : String,
    var forProfileUpdate: Boolean,
    var email: String
)
