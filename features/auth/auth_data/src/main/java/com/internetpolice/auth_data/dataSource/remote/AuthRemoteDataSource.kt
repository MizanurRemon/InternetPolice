package com.internetpolice.auth_data.dataSource.remote

import com.internetpolice.core.network.dto.AuthenticationDraftDto
import com.internetpolice.core.network.dto.RegistrationDto
import com.internetpolice.core.network.dto.CommonResponseDto
import com.internetpolice.core.network.dto.LoginDto
import com.internetpolice.core.network.dto.UserWaitingDto
import com.internetpolice.core.network.model.*


interface AuthRemoteDataSource {
    suspend fun getRegistrationResponse(registrationRequest: RegistrationRequest): RegistrationDto
    suspend fun getLoginResponse(loginRequest: LoginRequest): LoginDto
    suspend fun getVerifyTokenResponse(token: String): CommonResponseDto
    suspend fun resendVerificationEmail(resendVerification: ResendVerificationRequest): CommonResponseDto
    suspend fun resetPasswordLinkSend(resetPasswordRequest: ResetPasswordRequest): CommonResponseDto
    suspend fun passwordReset(newPasswordRequest: NewPasswordRequest): CommonResponseDto
    suspend fun isRegAllowCheck(): Boolean
    suspend fun userWaiting(userWaitingRequest: UserWaitingRequest): UserWaitingDto
    suspend fun updateEmail(updateEmailRequest: UpdateEmailRequest): LoginDto
    suspend fun authenticationDraftCheck(authenticationDraftRequest: AuthenticationDraftRequest): AuthenticationDraftDto
    suspend fun verificationEmailDraft(draftVerificationRequest: DraftVerificationRequest): CommonResponseDto
    suspend fun draftUserTokenVerify(token: String): LoginDto
}