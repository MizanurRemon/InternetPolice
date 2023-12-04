package com.internetpolice.profile_domain.use_cases

import com.internetpolice.profile_domain.model.ProgressModel
import com.internetpolice.profile_domain.repository.ProfileRepository

class ProgressUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(): Result<ProgressModel> {
        return profileRepository.userProgress()
    }
}