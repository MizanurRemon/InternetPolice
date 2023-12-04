package com.internetpolice.auth.auth_domain.use_cases

import com.internetpolice.auth.auth_domain.model.LoginModel
import com.internetpolice.auth.auth_domain.model.LoginResponse
import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.core.network.model.LoginRequest

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(loginModel: LoginModel): Result<LoginResponse> {
        return authRepository.getLoginResponse(
            LoginRequest(
                username = loginModel.email,
                password = loginModel.password
            )
        )
    }
}