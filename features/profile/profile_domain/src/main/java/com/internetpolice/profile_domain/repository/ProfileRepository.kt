package com.internetpolice.profile_domain.repository


import com.internetpolice.profile_domain.model.CommonModel
import com.internetpolice.profile_domain.model.CompleteProfileModel
import com.internetpolice.profile_domain.model.ProfileModel
import com.internetpolice.profile_domain.model.ProgressModel
import com.internetpolice.profile_domain.model.UserDataUpdateModel
import com.internetpolice.profile_domain.model.UserNameVerifyModel


interface ProfileRepository {
    suspend fun userNameVerify(name: String): Result<UserNameVerifyModel>
    suspend fun userDataUpdate(name: String, ageCategory: String): Result<UserDataUpdateModel>
    suspend fun completeProfile(profileModel: ProfileModel): Result<CompleteProfileModel>
    suspend fun userProgress(): Result<ProgressModel>
    suspend fun updateScore()
    suspend fun logOut(): Result<CommonModel>
    suspend fun deleteAccount(): Result<CommonModel>
    suspend fun passwordChange(password: String, newPassword: String): Result<CommonModel>
    suspend fun basicInfoUpdate(name: String, ageCategory: String): Result<UserDataUpdateModel>
}