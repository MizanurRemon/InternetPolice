package com.internetpolice.auth_data.mapper

import com.internetpolice.auth.auth_domain.model.LoginResponse
import com.internetpolice.auth.auth_domain.model.RegistrationResponse
import com.internetpolice.core.network.dto.LoginDto
import com.internetpolice.core.network.dto.RegistrationDto


fun RegistrationDto.toRegistrationResponse(): RegistrationResponse {
    return RegistrationResponse(
        userId = userId ?: 0,
    )
}

fun LoginDto.toLoginResponse(): LoginResponse {
    return LoginResponse(
        userId = userId,
        access_token = access_token,
        refresh_token = refresh_token,
        userProfile = this.userProfile
    )
}

