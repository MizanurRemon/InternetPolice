package com.internetpolice.profile_domain.use_cases

import com.internetpolice.profile_domain.model.CompleteProfileModel
import com.internetpolice.profile_domain.model.ProfileModel
import com.internetpolice.profile_domain.repository.ProfileRepository

class ProfileCompleteUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(profileModel: ProfileModel): Result<CompleteProfileModel > {
        return profileRepository.completeProfile(profileModel)
    }
}