package com.internetpolice.auth_data.mapper

import com.internetpolice.auth.auth_domain.model.AuthenticationDraftResponse
import com.internetpolice.core.network.dto.AuthenticationDraftDto

fun AuthenticationDraftDto.toResponse(): AuthenticationDraftResponse {
    return AuthenticationDraftResponse(
        userId = userId
    )
}