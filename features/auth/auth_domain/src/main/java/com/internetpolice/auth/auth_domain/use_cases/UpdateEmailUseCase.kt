package com.internetpolice.auth.auth_domain.use_cases

import com.internetpolice.auth.auth_domain.model.LoginResponse
import com.internetpolice.auth.auth_domain.model.UpdateEmailModel
import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.core.network.model.UpdateEmailRequest

class UpdateEmailUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String): Result<LoginResponse> {
        return authRepository.updateEmail(email)
    }
}