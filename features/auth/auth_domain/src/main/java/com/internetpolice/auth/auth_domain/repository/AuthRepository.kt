package com.internetpolice.auth.auth_domain.repository

import com.internetpolice.auth.auth_domain.model.AuthenticationDraftResponse
import com.internetpolice.auth.auth_domain.model.LoginResponse
import com.internetpolice.auth.auth_domain.model.RegistrationResponse
import com.internetpolice.auth.auth_domain.model.UserWaitingResponse
import com.internetpolice.core.network.dto.CommonResponseDto
import com.internetpolice.core.network.model.AuthenticationDraftRequest
import com.internetpolice.core.network.model.DraftVerificationRequest
import com.internetpolice.core.network.model.LoginRequest
import com.internetpolice.core.network.model.NewPasswordRequest
import com.internetpolice.core.network.model.RegistrationRequest
import com.internetpolice.core.network.model.ResetPasswordRequest
import com.internetpolice.core.network.model.UpdateEmailRequest
import com.internetpolice.core.network.model.UserWaitingRequest


interface AuthRepository {
    suspend fun getRegistrationResponse(registrationRequest: RegistrationRequest): Result<RegistrationResponse>
    suspend fun getLoginResponse(loginRequest: LoginRequest): Result<LoginResponse>
    suspend fun getVerifyTokenResponse(token: String): Result<CommonResponseDto>
    suspend fun resendVerificationEmail(email: String, forProfileUpdate: Boolean): Result<CommonResponseDto>
    suspend fun resetPasswordLinkSend(string: ResetPasswordRequest): Result<CommonResponseDto>
    suspend fun passwordReset(newPasswordRequest: NewPasswordRequest): Result<CommonResponseDto>
    suspend fun isRegAllowCheck(): Result<Boolean>
    suspend fun userWaiting(userWaitingRequest: UserWaitingRequest): Result<UserWaitingResponse>
    suspend fun updateEmail(email : String): Result<LoginResponse>
    suspend fun authenticationDraftCheck(authenticationDraftRequest: AuthenticationDraftRequest): Result<AuthenticationDraftResponse>
    suspend fun verificationEmailDraft() : Result<CommonResponseDto>
    suspend fun draftUserTokenVerify(email: String,token: String): Result<LoginResponse>
}