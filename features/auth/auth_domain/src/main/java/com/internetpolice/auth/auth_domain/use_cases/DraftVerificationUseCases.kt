package com.internetpolice.auth.auth_domain.use_cases

import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.core.network.dto.CommonResponseDto

class DraftVerificationUseCases(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<CommonResponseDto> {
        return authRepository.verificationEmailDraft()
    }
}