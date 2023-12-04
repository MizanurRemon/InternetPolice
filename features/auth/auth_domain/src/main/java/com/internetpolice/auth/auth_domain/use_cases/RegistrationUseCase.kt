package com.internetpolice.auth.auth_domain.use_cases

import com.internetpolice.auth.auth_domain.model.RegistrationModel
import com.internetpolice.auth.auth_domain.model.RegistrationResponse
import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.core.network.model.RegistrationRequest

class RegistrationUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(registrationModel: RegistrationModel): Result<RegistrationResponse> {
        return authRepository.getRegistrationResponse(
            RegistrationRequest(
                ageCategory = registrationModel.ageCategory,
                email = registrationModel.email,
                password = registrationModel.password,
                confirmPassword = registrationModel.confirmPassword,
                isAgreedToTermsAndConditions = registrationModel.isAgreedToTermsAndConditions,
                language = registrationModel.language,
            )
        )
    }
}