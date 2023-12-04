package com.internetpolice.auth.auth_domain.use_cases

import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.core.network.dto.CommonResponseDto

class ResendVerificationEmilUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email : String, forProfileUpdate: Boolean): Result<CommonResponseDto> {
        return authRepository.resendVerificationEmail(email, forProfileUpdate)

    }
}