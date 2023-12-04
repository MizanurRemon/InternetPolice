package com.internetpolice.auth.auth_domain.use_cases

import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.core.network.dto.CommonResponseDto
import com.internetpolice.core.network.model.NewPasswordRequest

class ResetPasswordUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        password: String,
        confirmPassword: String,
    ): Result<CommonResponseDto> {
        return authRepository.passwordReset(NewPasswordRequest(password, confirmPassword))
    }
}