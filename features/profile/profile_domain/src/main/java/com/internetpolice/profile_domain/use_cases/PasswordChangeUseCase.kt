package com.internetpolice.profile_domain.use_cases

import com.internetpolice.profile_domain.model.CommonModel
import com.internetpolice.profile_domain.repository.ProfileRepository

class PasswordChangeUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(password: String, newPassword: String): Result<CommonModel> {
        return profileRepository.passwordChange(password, newPassword)
    }
}