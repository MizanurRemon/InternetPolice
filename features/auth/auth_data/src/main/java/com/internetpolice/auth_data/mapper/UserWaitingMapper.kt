package com.internetpolice.auth_data.mapper

import com.internetpolice.core.network.dto.UserWaitingDto
import com.internetpolice.auth.auth_domain.model.UserWaitingResponse

fun UserWaitingDto.toResponse(): UserWaitingResponse {
    return UserWaitingResponse(
        id = id ?: 0
    )
}