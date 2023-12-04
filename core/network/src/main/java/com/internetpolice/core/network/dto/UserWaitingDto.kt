package com.internetpolice.core.network.dto

import kotlinx.serialization.Serializable


@Serializable
data class UserWaitingDto(
   // var createdDate: Int? = null,
    var updatedDate: Int? = null,
    var createdBy: String? = "",
    var updatedBy: String? = "",
    var deletedAt: Int? = null,
    var id: Int? = null,
    var email: String? = "",
    var apiSyncStatus: String? = "",
    var referenceId: Int? = null
)
