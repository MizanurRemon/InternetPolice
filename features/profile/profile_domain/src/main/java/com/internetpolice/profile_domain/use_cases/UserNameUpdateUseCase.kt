package com.internetpolice.profile_domain.use_cases

import com.internetpolice.profile_domain.model.CompleteProfileModel
import com.internetpolice.profile_domain.model.ProfileModel
import com.internetpolice.profile_domain.model.UserNameVerifyModel
import com.internetpolice.profile_domain.repository.ProfileRepository

class UserNameUpdateUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(name:String): Result<UserNameVerifyModel > {
        return profileRepository.userNameVerify(name)
    }
}