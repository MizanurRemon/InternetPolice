package com.internetpolice.auth.auth_domain.use_cases

import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.core.network.dto.CommonResponseDto
import com.internetpolice.core.network.model.ResetPasswordRequest

class ResetPasswordLinkSendUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String): Result<CommonResponseDto> {
        return authRepository.resetPasswordLinkSend(ResetPasswordRequest(email))

    }
}