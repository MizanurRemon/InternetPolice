package com.internetpolice.core.network

import com.internetpolice.core.network.dto.*
import com.internetpolice.core.network.model.*
import retrofit2.http.*

interface PrivateApiService {

    @POST("api/verification/email")
    suspend fun resendVerificationCode(@Body resendVerification: ResendVerificationRequest): CommonResponseDto

    @GET("api/domain/{domain}")
    suspend fun domainDetails(@Path("domain") domain: String): DomainDetailsDto

    @POST("api/votes")
    suspend fun reportDomain(@Body reportRequest: ReportRequest): ReportResponse

    @GET("api/voteCategory/{language}/{categoryType}")
    suspend fun getCategoryTypeList(
        @Path("language") language: String,
        @Path("categoryType") categoryType: String,
    ): List<String>

    @POST("api/extension/profile/report-issue")
    suspend fun reportProblem(
        @Body reportProblemRequest: ReportProblemRequest,
    ): ReportProblemDto

    @PUT("api/extension/profile")
    suspend fun completeProfile(
        @Body completeProfileRequest: CompleteProfileRequest,
    ): CompleteProfileDto

    @PUT("api/extension/profile")
    suspend fun userNameVerify(
        @Body updateUserNameRequest: UpdateUserNameRequest,
    ): UserNameVerifyDto

    @PUT("api/extension/profile")
    suspend fun userDataUpdate(
        @Body userDataUpdateRequest: UserDataUpdateRequest,
    ): UserDataUpdateDto

    @GET("api/extension/profile/points/{userId}")
    suspend fun userPoints(
        @Path("userId") userId: Int,
    ): UserPointsDto

    @GET("api/extension/profile/votes/{userId}")
    suspend fun voteList(
        @Path("userId") userId: Int,
    ): VoteListDto

    @POST("api/extension/profile/password-change")
    suspend fun passwordChange(
        @Body resetRequest: PasswordChangeRequest,
    ): PasswordChangeDto

    @DELETE("api/extension/profile/{userId}")
    suspend fun deleteAccount(
        @Path("userId") userId: Int,
    )

    @POST("public/api/ecommerce/scrap-data")
    suspend fun getScrapData(
        @Body scrapDataRequest: ScrapDataRequest,
    ): ScrapDataDto

    @POST("api/extension/profile/change-email")
    suspend fun updateEmail(
        @Body updateEmailRequest: UpdateEmailRequest
    ): LoginDto

    @PUT("api/extension/profile/basic-update/{userId}")
    suspend fun basicInfoUpdate(
        @Path("userId") userId: Int,
        @Body basicInfoRequest: BasicInfoRequest
    ): UserDataUpdateDto

    @POST("api/contact-us")
    suspend fun sendHelpContactDescription(
        @Body descriptionRequest: SendDescriptionRequest
    ): SendDescriptionDto

    @PUT("api/extension/profile/language/{userId}/{language}")
    suspend fun updateProfileLanguage(
        @Path("userId") userId: Int,
        @Path("language") language: String
    ): UserProfile
}