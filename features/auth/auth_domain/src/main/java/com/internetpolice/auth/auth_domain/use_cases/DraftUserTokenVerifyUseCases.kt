package com.internetpolice.auth.auth_domain.use_cases

import com.internetpolice.auth.auth_domain.model.LoginResponse
import com.internetpolice.auth.auth_domain.repository.AuthRepository
class DraftUserTokenVerifyUseCases(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, token: String): Result<LoginResponse> {
        return authRepository.draftUserTokenVerify(email, token)
    }
}