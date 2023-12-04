package com.internetpolice.profile_domain.use_cases

import com.internetpolice.profile_domain.model.CommonModel
import com.internetpolice.profile_domain.model.CompleteProfileModel
import com.internetpolice.profile_domain.model.ProfileModel
import com.internetpolice.profile_domain.model.UserNameVerifyModel
import com.internetpolice.profile_domain.repository.ProfileRepository

class LogOutUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(): Result<CommonModel > {
        return profileRepository.logOut()
    }
}