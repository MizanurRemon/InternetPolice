package com.internetpolice.profile_domain.use_cases

import com.internetpolice.profile_domain.repository.ProfileRepository

class UpdateScoreUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke() {
        return profileRepository.updateScore()
    }
}