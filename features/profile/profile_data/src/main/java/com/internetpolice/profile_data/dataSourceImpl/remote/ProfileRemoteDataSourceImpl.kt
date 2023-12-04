package com.internetpolice.profile_data.dataSourceImpl.remote

import com.internetpolice.core.network.PrivateApiService
import com.internetpolice.core.network.PublicApiService
import com.internetpolice.core.network.dto.CompleteProfileDto
import com.internetpolice.core.network.dto.PasswordChangeDto
import com.internetpolice.core.network.dto.UserDataUpdateDto
import com.internetpolice.core.network.dto.UserNameVerifyDto
import com.internetpolice.core.network.dto.UserPointsDto
import com.internetpolice.core.network.dto.VoteListDto
import com.internetpolice.core.network.model.BasicInfoRequest
import com.internetpolice.core.network.model.CompleteProfileRequest
import com.internetpolice.core.network.model.LogOutRequest
import com.internetpolice.core.network.model.PasswordChangeRequest
import com.internetpolice.core.network.model.UpdateUserNameRequest
import com.internetpolice.core.network.model.UserDataUpdateRequest
import com.internetpolice.profile_data.dataSource.remote.ProfileRemoteDataSource


class ProfileRemoteDataSourceImpl(
    private val publicApiService: PublicApiService,
    private val privateApiService: PrivateApiService,
) : ProfileRemoteDataSource {
    override suspend fun userNameVerify(updateUserNameRequest: UpdateUserNameRequest): UserNameVerifyDto {
        return privateApiService.userNameVerify(updateUserNameRequest)
    }

    override suspend fun userDataUpdate(userDataUpdateRequest: UserDataUpdateRequest): UserDataUpdateDto {
        return privateApiService.userDataUpdate(userDataUpdateRequest)
    }

    override suspend fun completeProfile(completeProfileRequest: CompleteProfileRequest): CompleteProfileDto {
        return privateApiService.completeProfile(completeProfileRequest)
    }

    override suspend fun userPoints(userId: Int): UserPointsDto {
        return privateApiService.userPoints(userId)
    }

    override suspend fun userVotes(userId: Int): VoteListDto {
        return privateApiService.voteList(userId)
    }

    override suspend fun logOut(logOutRequest: LogOutRequest) {
        publicApiService.logOut(logOutRequest)
    }

    override suspend fun deleteAccount(userId: Int) {
        privateApiService.deleteAccount(userId)
    }

    override suspend fun passwordReset(
        passwordResetRequest: PasswordChangeRequest,
    ): PasswordChangeDto {
        return privateApiService.passwordChange(passwordResetRequest)
    }

    override suspend fun basicInfoUpdate(
        userId: Int,
        basicInfoRequest: BasicInfoRequest
    ): UserDataUpdateDto {
        return privateApiService.basicInfoUpdate(userId, basicInfoRequest)
    }

}