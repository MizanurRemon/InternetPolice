package com.internetpolice.profile_domain.use_cases

import com.internetpolice.profile_domain.model.UserDataUpdateModel
import com.internetpolice.profile_domain.repository.ProfileRepository

class UserDataUpdateUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(name: String, ageCategory: String): Result<UserDataUpdateModel> {
        return profileRepository.userDataUpdate(name, ageCategory)
    }
}