package com.internetpolice.auth.auth_domain.use_cases

import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.core.network.dto.CommonResponseDto

class VerifyTokenUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(token: String): Result<CommonResponseDto> {
        return authRepository.getVerifyTokenResponse(token)

    }
}