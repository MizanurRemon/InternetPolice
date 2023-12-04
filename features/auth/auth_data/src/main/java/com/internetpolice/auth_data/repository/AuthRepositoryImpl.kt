package com.internetpolice.auth_data.repository

import android.util.Log
import com.internetpolice.auth.auth_domain.model.AuthenticationDraftResponse
import com.internetpolice.auth.auth_domain.model.LoginResponse
import com.internetpolice.auth.auth_domain.model.RegistrationResponse
import com.internetpolice.auth.auth_domain.model.UserWaitingResponse
import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.auth_data.dataSource.local.AuthLocalDataSource
import com.internetpolice.auth_data.dataSource.remote.AuthRemoteDataSource
import com.internetpolice.auth_data.mapper.toLoginResponse
import com.internetpolice.auth_data.mapper.toRegistrationResponse
import com.internetpolice.auth_data.mapper.toResponse
import com.internetpolice.core.common.util.DEFAULT_LANGUAGE_TAG
import com.internetpolice.core.common.util.ENGLISH_TAG
import com.internetpolice.core.common.util.LanguageTagEnum
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.dto.CommonResponseDto
import com.internetpolice.core.network.model.*
import com.internetpolice.core.network.util.NetworkHandler
import com.internetpolice.database.entity.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.HttpException


class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private val networkHandler: NetworkHandler,
) : AuthRepository {
    override suspend fun getRegistrationResponse(registrationRequest: RegistrationRequest): Result<RegistrationResponse> {
        return if (networkHandler.isNetworkAvailable()) {
            try {

                val tag = preferenceDataStoreHelper.getFirstPreference(
                    PreferenceDataStoreConstants.LANGUAGE_TAG,
                    DEFAULT_LANGUAGE_TAG
                )

                val registrationRequestData =
                    registrationRequest.copy(
                        language = LanguageTagEnum.values().first { it.tag == tag }
                            .name

                    )

                val registrationDto =
                    authRemoteDataSource.getRegistrationResponse(registrationRequestData)

                val registrationResponse = registrationDto.toRegistrationResponse()

                /* preferenceDataStoreHelper.putPreference(
                     PreferenceDataStoreConstants.ACCESS_TOKEN,
                     registrationResponse.access_token
                 )
                 preferenceDataStoreHelper.putPreference(
                     PreferenceDataStoreConstants.REFRESH_TOKEN,
                     registrationResponse.refresh_token
                 )*/

                authLocalDataSource.deleteUsers()
                authLocalDataSource.saveUser(
                    UserEntity(
                        id = 0,
                        userId = registrationResponse.userId,
                        email = registrationRequestData.email,
                        ageCategory = registrationRequestData.ageCategory,
                        language = registrationRequestData.language,
                        isUserGiveConsent = false
                    )
                )


                Result.success(registrationResponse)

            } catch (e: HttpException) {
                val json =
                    Json { isLenient = true;ignoreUnknownKeys = true }
                val obj = e.response()?.errorBody()?.string()
                    ?.let {
                        json.decodeFromString<RegistrationErrorModel>(it)
                    }
                val message: String = if (obj?.statusCode == 429) {
                    obj.message.let { it.toString() }
                } else {
                    obj?.email?.get(0).let { "User email already $it" }
                }


                val throwable =
                    Throwable(message = message ?: e.message(), cause = e.cause)
                Result.failure(throwable)

            } catch (e: Exception) {
                Result.failure(e)

            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun getLoginResponse(loginRequest: LoginRequest): Result<LoginResponse> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val loginDto = authRemoteDataSource.getLoginResponse(loginRequest)

                val loginResponse = loginDto.toLoginResponse()
                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.ACCESS_TOKEN,
                    loginResponse.access_token
                )
                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.REFRESH_TOKEN,
                    loginResponse.refresh_token
                )

                authLocalDataSource.deleteUsers()
                authLocalDataSource.saveUser(
                    UserEntity(
                        id = loginResponse.userProfile.id,
                        userId = loginResponse.userId,
                        email = loginRequest.username,
                        ageCategory = loginResponse.userProfile.ageCategory,
                        avatarImageName = loginResponse.userProfile.avatarImageName,
                        avatarImagePath = if (loginResponse.userProfile.avatarImagePath != "") loginResponse.userProfile.avatarImagePath else null,
                        color = loginResponse.userProfile.color,
                        hairStyle = loginResponse.userProfile.hairStyle,
                        faceStyle = loginResponse.userProfile.faceStyle,
                        hairColor = loginResponse.userProfile.hairColor,
                        language = loginResponse.userProfile.language,
                        rank = loginResponse.userProfile.rank,
                        nickname = loginResponse.userProfile.nickname,
                        gender = loginResponse.userProfile.gender,
                        isProfileCompleted = loginResponse.userProfile.isProfileCompleted,
                        isUserGiveConsent = false

                    )
                )
                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.IS_LOGGED_IN,
                    true
                )
                Result.success(loginResponse)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Result.failure(Throwable(e.code().toString()))
                } else {
                    val json =
                        Json { isLenient = true;ignoreUnknownKeys = true }
                    val obj = e.response()?.errorBody()?.string()
                        ?.let {
                            json.decodeFromString<CommonErrorModel>(it)
                        }

                    val message = obj?.error

                    val throwable =
                        Throwable(
                            message = message ?: "Something went wrong",
                            cause = e.cause
                        )
                    Result.failure(throwable)
                }


            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun authenticationDraftCheck(authenticationDraftRequest: AuthenticationDraftRequest): Result<AuthenticationDraftResponse> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val responseDto =
                    authRemoteDataSource.authenticationDraftCheck(authenticationDraftRequest)

                val response = responseDto.toResponse()

                authLocalDataSource.deleteUsers()
                authLocalDataSource.saveUser(
                    UserEntity(
                        id = 0,
                        userId = response.userId,
                        email = authenticationDraftRequest.username,
                    )
                )

                Result.success(response)
            } catch (e: HttpException) {

                if (e.code() == 404) {
                    Result.failure(Throwable("User not found"))
                } else if (e.code() == 401) {
                    Result.failure(Throwable("Password not matched"))
                } else {
                    val json =
                        Json { isLenient = true;ignoreUnknownKeys = true }
                    val obj = e.response()?.errorBody()?.string()
                        ?.let {
                            json.decodeFromString<CommonErrorModel>(it)
                        }

                    val message = obj?.error

                    val throwable =
                        Throwable(
                            message = message ?: "Something went wrong",
                            cause = e.cause
                        )
                    Result.failure(throwable)
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }

    }

    override suspend fun getVerifyTokenResponse(token: String): Result<CommonResponseDto> {
        return if (networkHandler.isNetworkAvailable()) {
            try {

                val response = authRemoteDataSource.getVerifyTokenResponse(token)
                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.IS_LOGGED_IN,
                    true
                )
                Result.success(response)
            } catch (e: HttpException) {
                try {
                    val json =
                        Json { isLenient = true;ignoreUnknownKeys = true }
                    val obj = e.response()?.errorBody()?.string()
                        ?.let {
                            json.decodeFromString<CommonErrorModel>(it)
                        }

                    val message = obj?.error?.let { it }

                    val throwable =
                        Throwable(message = message ?: "Something went wrong", cause = e.cause)
                    Result.failure(throwable)
                } catch (e: Exception) {
                    val throwable =
                        Throwable(message = "Something went wrong")
                    Result.failure(throwable)
                }

            } catch (e: Exception) {
                val throwable =
                    Throwable(message = "Something went wrong")
                Result.failure(throwable)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun resendVerificationEmail(
        newEmail: String,
        forProfileUpdate: Boolean
    ): Result<CommonResponseDto> {

        return if (networkHandler.isNetworkAvailable()) {
            try {
                val user = authLocalDataSource.getUsers().first()

                val response = authRemoteDataSource.resendVerificationEmail(
                    ResendVerificationRequest(
                        username = user[0].email,
                        email = newEmail,// user[0].email
                        forProfileUpdate = forProfileUpdate
                    )
                )
                Result.success(response)
            } catch (e: HttpException) {
                try {
                    val json =
                        Json { isLenient = true;ignoreUnknownKeys = true }
                    val obj = e.response()?.errorBody()?.string()
                        ?.let {
                            json.decodeFromString<CommonErrorModel>(it)
                        }
                    val message = obj?.error?.let { it }

                    val throwable =
                        Throwable(message = message ?: "Something went wrong", cause = e.cause)
                    Result.failure(throwable)
                } catch (e: Exception) {
                    val throwable =
                        Throwable(message = "Something went wrong")
                    Result.failure(throwable)
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }

    }

    override suspend fun resetPasswordLinkSend(resetPasswordRequest: ResetPasswordRequest): Result<CommonResponseDto> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val response = authRemoteDataSource.resetPasswordLinkSend(
                    resetPasswordRequest
                )
                Result.success(response)
            } catch (e: HttpException) {
                val json =
                    Json { isLenient = true;ignoreUnknownKeys = true }
                val obj = e.response()?.errorBody()?.string()
                    ?.let {
                        json.decodeFromString<CommonErrorModel>(it)
                    }
                val message = obj?.error?.let { it }

                val throwable =
                    Throwable(message = message ?: "Something went wrong", cause = e.cause)
                Result.failure(throwable)

            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun passwordReset(newPasswordRequest: NewPasswordRequest): Result<CommonResponseDto> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val response = authRemoteDataSource.passwordReset(
                    newPasswordRequest
                )
                Result.success(response)
            } catch (e: HttpException) {
                val json =
                    Json { isLenient = true;ignoreUnknownKeys = true }
                val obj = e.response()?.errorBody()?.string()
                    ?.let {
                        json.decodeFromString<CommonErrorModel>(it)
                    }
                val message = obj?.error?.let { it }

                val throwable =
                    Throwable(message = message ?: "Something went wrong", cause = e.cause)
                Result.failure(throwable)

            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun isRegAllowCheck(): Result<Boolean> {
        return if (networkHandler.isNetworkAvailable()) {

            try {
                val response = authRemoteDataSource.isRegAllowCheck()
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }

        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun userWaiting(userWaitingRequest: UserWaitingRequest): Result<UserWaitingResponse> {
        return if (networkHandler.isNetworkAvailable()) {

            try {
                val userWaitingDto = authRemoteDataSource.userWaiting(userWaitingRequest)

                val response = userWaitingDto.toResponse()

                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }

        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun updateEmail(email: String): Result<LoginResponse> {
        return if (networkHandler.isNetworkAvailable()) {
            try {

                val user = authLocalDataSource.getUsers().first()
                val loginDto = authRemoteDataSource.updateEmail(
                    UpdateEmailRequest(
                        newEmail = email,
                        userId = user[0].userId
                    )
                )

                val loginResponse = loginDto.toLoginResponse()
                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.ACCESS_TOKEN,
                    loginResponse.access_token
                )
                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.REFRESH_TOKEN,
                    loginResponse.refresh_token
                )

                authLocalDataSource.deleteUsers()
                authLocalDataSource.saveUser(
                    UserEntity(
                        id = loginResponse.userProfile.id,
                        userId = loginResponse.userId,
                        email = email,
                        ageCategory = loginResponse.userProfile.ageCategory,
                        avatarImageName = loginResponse.userProfile.avatarImageName,
                        avatarImagePath = loginResponse.userProfile.avatarImagePath,
                        color = loginResponse.userProfile.color,
                        hairStyle = loginResponse.userProfile.hairStyle,
                        faceStyle = loginResponse.userProfile.faceStyle,
                        hairColor = loginResponse.userProfile.hairColor,
                        language = loginResponse.userProfile.language,
                        rank = loginResponse.userProfile.rank,
                        nickname = loginResponse.userProfile.nickname,
                        gender = loginResponse.userProfile.gender,
                        isProfileCompleted = loginResponse.userProfile.isProfileCompleted,
                        totalPoints = user[0].totalPoints
                    )
                )
                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.IS_LOGGED_IN,
                    true
                )
                Result.success(loginResponse)


            } catch (e: Exception) {
                val throwable =
                    Throwable(message = "Something went wrong")
                Result.failure(throwable)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun verificationEmailDraft(): Result<CommonResponseDto> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val user = authLocalDataSource.getUsers().first()
                val responseDto =
                    authRemoteDataSource.verificationEmailDraft(
                        DraftVerificationRequest(
                            userId = user[0].userId.toString(),
                            username = user[0].email,
                            email = user[0].email,
                            forProfileUpdate = false
                        )
                    )

                Result.success(responseDto)
            } catch (e: Exception) {
                val throwable =
                    Throwable(message = "Something went wrong")
                Result.failure(throwable)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun draftUserTokenVerify(email: String, token: String): Result<LoginResponse> {
        return if (networkHandler.isNetworkAvailable()) {
            try {

                val responseDto = authRemoteDataSource.draftUserTokenVerify(token)
                val loginResponse = responseDto.toLoginResponse()


                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.ACCESS_TOKEN,
                    loginResponse.access_token
                )
                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.REFRESH_TOKEN,
                    loginResponse.refresh_token
                )

                authLocalDataSource.deleteUsers()
                authLocalDataSource.saveUser(
                    UserEntity(
                        id = loginResponse.userProfile.id,
                        userId = loginResponse.userId,
                        email = email,
                        ageCategory = loginResponse.userProfile.ageCategory,
                        avatarImageName = loginResponse.userProfile.avatarImageName,
                        avatarImagePath = if (loginResponse.userProfile.avatarImagePath != "") loginResponse.userProfile.avatarImagePath else null,
                        color = loginResponse.userProfile.color,
                        hairStyle = loginResponse.userProfile.hairStyle,
                        faceStyle = loginResponse.userProfile.faceStyle,
                        hairColor = loginResponse.userProfile.hairColor,
                        language = loginResponse.userProfile.language,
                        rank = loginResponse.userProfile.rank,
                        nickname = loginResponse.userProfile.nickname,
                        gender = loginResponse.userProfile.gender,
                        isProfileCompleted = loginResponse.userProfile.isProfileCompleted

                    )
                )
                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.IS_LOGGED_IN,
                    true
                )

                Result.success(loginResponse)

            } catch (e: Exception) {
                val throwable =
                    Throwable(message = "Something went wrong")
                Result.failure(throwable)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

}