package com.internetpolice.auth.auth_domain.use_cases

import com.internetpolice.auth.auth_domain.repository.AuthRepository

class RegistrationAllowUseCases(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<Boolean>{
        return authRepository.isRegAllowCheck()
    }
}