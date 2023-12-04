package com.internetpolice.profile_data.dataSource.remote

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


interface ProfileRemoteDataSource {
    suspend fun userNameVerify(updateUserNameRequest: UpdateUserNameRequest): UserNameVerifyDto
    suspend fun userDataUpdate(userDataUpdateRequest: UserDataUpdateRequest): UserDataUpdateDto
    suspend fun completeProfile(completeProfileRequest: CompleteProfileRequest): CompleteProfileDto
    suspend fun userPoints(userId: Int): UserPointsDto
    suspend fun userVotes(userId: Int): VoteListDto
    suspend fun logOut(logOutRequest: LogOutRequest)
    suspend fun deleteAccount(userId: Int)
    suspend fun passwordReset(passwordResetRequest: PasswordChangeRequest): PasswordChangeDto
    suspend fun basicInfoUpdate(userId: Int, basicInfoRequest: BasicInfoRequest): UserDataUpdateDto
}