package com.internetpolice.core.network

import com.internetpolice.core.network.dto.AuthenticationDraftDto
import com.internetpolice.core.network.dto.CommonResponseDto
import com.internetpolice.core.network.dto.DomainDto
import com.internetpolice.core.network.dto.DomainNameDetailsDto
import com.internetpolice.core.network.dto.LoginDto
import com.internetpolice.core.network.dto.RegistrationDto
import com.internetpolice.core.network.dto.TokenDto
import com.internetpolice.core.network.dto.UserWaitingDto
import com.internetpolice.core.network.model.*
import retrofit2.http.*

interface PublicApiService {
    @POST("public/api/extension/users")
    suspend fun registration(@Body registrationRequest: RegistrationRequest): RegistrationDto

    @GET("public/api/verification/email/token/{token}")
    suspend fun tokenVerify(@Path("token") token: String): CommonResponseDto

    @POST("token/refresh")
    suspend fun refreshToken(
        @Body tokenRequest: TokenRequest,
    ): TokenDto

    @POST("authenticate")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): LoginDto

    @POST("authenticate-draft")
    suspend fun authenticationDraftCheck(
        @Body authenticationDraftRequest: AuthenticationDraftRequest
    ): AuthenticationDraftDto

    @POST("public/api/verification/email-draft")
    suspend fun verificationEmailDraft(@Body draftVerificationRequest: DraftVerificationRequest): CommonResponseDto

    @GET("public/api/verification/email/token-draft/{token}")
    suspend fun draftUserTokenVerify(
        @Path("token") token: String
    ): LoginDto

    @GET("public/api/votes/domain-names/{partialText}")
    suspend fun getDomainList(
        @Path("partialText", encoded = true) partialText: String,
    ): List<DomainDto>

    @POST("public/api/reset/password")
    suspend fun resetPasswordLinkSend(
        @Body resetPasswordRequest: ResetPasswordRequest,
    ): CommonResponseDto

    @POST("public/api/reset/password")
    suspend fun passwordReset(
        @Body newPasswordRequest: NewPasswordRequest,
    ): CommonResponseDto

    @POST("logout/full")
    suspend fun logOut(
        @Body logOutRequest: LogOutRequest,
    )

    @GET("public/api/votes/domain/{domainName}")
    suspend fun getDomainDetails(
        @Path("domainName", encoded = true) domainName: String,
    ): DomainNameDetailsDto

    @POST("public/api/extension/is-reg-allowed")
    suspend fun isRegAllowCheck(): Boolean

    @POST("public/api/user-waiting")
    suspend fun userWaiting(
        @Body userWaitingRequest: UserWaitingRequest
    ): UserWaitingDto

}