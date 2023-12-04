package com.internetpolice.profile_domain.use_cases

import com.internetpolice.profile_domain.model.CommonModel
import com.internetpolice.profile_domain.repository.ProfileRepository

class DeleteAccountUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(): Result<CommonModel> {
        return profileRepository.deleteAccount()
    }
}