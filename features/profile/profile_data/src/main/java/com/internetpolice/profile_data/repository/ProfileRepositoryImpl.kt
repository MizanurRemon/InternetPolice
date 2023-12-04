package com.internetpolice.profile_data.repository

import android.util.Log
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.model.BasicInfoRequest
import com.internetpolice.core.network.model.CommonErrorModel
import com.internetpolice.core.network.model.CompleteProfileRequest
import com.internetpolice.core.network.model.LogOutRequest
import com.internetpolice.core.network.model.PasswordChangeRequest
import com.internetpolice.core.network.model.UpdateUserNameRequest
import com.internetpolice.core.network.model.UserDataUpdateRequest
import com.internetpolice.core.network.util.NetworkHandler
import com.internetpolice.database.entity.UserEntity
import com.internetpolice.profile_data.dataSource.local.ProfileLocalDataSource
import com.internetpolice.profile_data.dataSource.remote.ProfileRemoteDataSource
import com.internetpolice.profile_data.mapper.toCompleteProfileModel
import com.internetpolice.profile_data.mapper.toUserDataUpdateModel
import com.internetpolice.profile_data.mapper.toUserNameVerifyModel
import com.internetpolice.profile_data.mapper.toUserPointModels
import com.internetpolice.profile_data.mapper.toVoteModels
import com.internetpolice.profile_domain.model.CommonModel
import com.internetpolice.profile_domain.model.CompleteProfileModel
import com.internetpolice.profile_domain.model.ProfileModel
import com.internetpolice.profile_domain.model.ProgressModel
import com.internetpolice.profile_domain.model.UserDataUpdateModel
import com.internetpolice.profile_domain.model.UserNameVerifyModel
import com.internetpolice.profile_domain.repository.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.HttpException

class ProfileRepositoryImpl(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private val networkHandler: NetworkHandler,
) : ProfileRepository {
    override suspend fun userNameVerify(name: String): Result<UserNameVerifyModel> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val user = profileLocalDataSource.getUsers().first()

                val response = profileRemoteDataSource.userNameVerify(
                    UpdateUserNameRequest(
                        userId = user[0].userId, nickname = name
                    )
                ).toUserNameVerifyModel()


                profileLocalDataSource.saveUser(user[0].copy(nickname = response.nickname))

                Result.success(
                    response
                )

            } catch (e: HttpException) {
                val json = Json { isLenient = true;ignoreUnknownKeys = true }
                val code = e.code()
                var message: String?
                if (code == 406) {
                    message = "Username  already taken"
                } else if (code == 451) {
                    message = "Unavailable for legal reasons"
                } else if (code == 417) {
                    message = "Expectation failed"
                } else {
                    val obj = e.response()?.errorBody()?.string()?.let {
                        json.decodeFromString<CommonErrorModel>(it)
                    }
                    message = obj?.error?.let { it }
                }

                val throwable =
                    Throwable(message = message ?: "Something went wrong", cause = e.cause)
                Result.failure(throwable)

            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            val throwable = Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun userDataUpdate(
        name: String,
        ageCategory: String,
    ): Result<UserDataUpdateModel> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val user = profileLocalDataSource.getUsers().first()

                val response = profileRemoteDataSource.userDataUpdate(
                    UserDataUpdateRequest(
                        userId = user[0].userId, nickname = name, ageCategory = ageCategory
                    )
                ).toUserDataUpdateModel()


                profileLocalDataSource.saveUser(
                    user[0].copy(
                        nickname = response.nickname,
                        ageCategory = response.ageCategory,
                    )
                )

                Result.success(
                    response
                )

            } catch (e: HttpException) {
                val json = Json { isLenient = true;ignoreUnknownKeys = true }
                val code = e.code()
                var message: String?
                if (code == 406) {
                    message = "Username  already taken"
                } else if (code == 451) {
                    message = "Unavailable for legal reasons"
                } else if (code == 417) {
                    message = "Expectation failed"
                } else {
                    val obj = e.response()?.errorBody()?.string()?.let {
                        json.decodeFromString<CommonErrorModel>(it)
                    }
                    message = obj?.error?.let { it }
                }

                val throwable =
                    Throwable(message = message ?: "Something went wrong", cause = e.cause)
                Result.failure(throwable)

            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            val throwable = Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun completeProfile(profileModel: ProfileModel): Result<CompleteProfileModel> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val user = profileLocalDataSource.getUsers().first()

                val response = profileRemoteDataSource.completeProfile(
                    CompleteProfileRequest(
                        color = profileModel.color,
                        hairColor = profileModel.hairColor,
                        hairStyle = profileModel.hairStyle,
                        userId = user[0].userId,
                        avatarImageName = "${profileModel.color}_${profileModel.faceStyle}_${profileModel.hairStyle}_${profileModel.hairColor}.png",
                        faceStyle = profileModel.faceStyle,
                        gender = profileModel.gender,
                        nickname = user[0].nickname ?: "",
                    )
                )

                val userPoints = profileRemoteDataSource.userPoints(user[0].userId)

                profileLocalDataSource.saveUser(
                    UserEntity(
                        id = response.id,
                        userId = response.userId,
                        nickname = response.nickname,
                        email = user[0].email,
                        color = response.color,
                        hairStyle = response.hairStyle,
                        faceStyle = response.faceStyle,
                        hairColor = response.hairColor,
                        gender = response.gender,
                        rank = response.rank,
                        avatarImageName = response.avatarImageName,
                        avatarImagePath = response.avatarImagePath,
                        language = response.language,
                        isProfileCompleted = response.isProfileCompleted,
                        ageCategory = response.ageCategory,
                        totalPoints = userPoints.userPoints.totalPoints,

                        )
                )

                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.IS_PROFILE_COMPLETE, response.isProfileCompleted
                )

                val completeProfileModel = response.toCompleteProfileModel()

                Result.success(
                    completeProfileModel.copy(totalPoints = userPoints.userPoints.totalPoints)
                )

            } catch (e: HttpException) {
                val json = Json { isLenient = true;ignoreUnknownKeys = true }
                val obj = e.response()?.errorBody()?.string()?.let {
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
            val throwable = Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun userProgress(): Result<ProgressModel> {
        return if (networkHandler.isNetworkAvailable()) {
            try {

                val user = profileLocalDataSource.getUsers().first()

                val userPoints = profileRemoteDataSource.userPoints(user[0].userId)
                val userVoteList = profileRemoteDataSource.userVotes(user[0].userId)

                profileLocalDataSource.saveUser(
                    user[0].copy(
                        totalPoints = userPoints.userPoints.totalPoints,
                    )
                )

                Result.success(
                    ProgressModel(
                        points = userPoints.toUserPointModels(), votes = userVoteList.toVoteModels()
                    )
                )

            } catch (e: HttpException) {
                val json = Json { isLenient = true;ignoreUnknownKeys = true }
                val obj = e.response()?.errorBody()?.string()?.let {
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
            val throwable = Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun updateScore() {
        if (networkHandler.isNetworkAvailable()) {
            try {
                val user = profileLocalDataSource.getUsers().first()

                val userPoints = profileRemoteDataSource.userPoints(user[0].userId)

                profileLocalDataSource.saveUser(
                    user[0].copy(
                        totalPoints = userPoints.userPoints.totalPoints,
                    )
                )

            } catch (e: Exception) {
            }
        }
    }

    override suspend fun logOut(): Result<CommonModel> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val refreshToken = preferenceDataStoreHelper.getFirstPreference(
                    PreferenceDataStoreConstants.REFRESH_TOKEN, ""
                )
                profileRemoteDataSource.logOut(
                    LogOutRequest(refresh_token = refreshToken)
                )

                //preferenceDataStoreHelper.clearAllPreference()
                preferenceDataStoreHelper.removePreference(PreferenceDataStoreConstants.IS_LOGGED_IN)
                preferenceDataStoreHelper.removePreference(PreferenceDataStoreConstants.REFRESH_TOKEN)
                preferenceDataStoreHelper.removePreference(PreferenceDataStoreConstants.ACCESS_TOKEN)
                preferenceDataStoreHelper.removePreference(PreferenceDataStoreConstants.IS_PROFILE_COMPLETE)
                preferenceDataStoreHelper.removePreference(PreferenceDataStoreConstants.IS_Notification_Enable)

                profileLocalDataSource.deleteUsers()

                Result.success(
                    CommonModel(result = "success")
                )

            } catch (e: HttpException) {
                val json = Json { isLenient = true;ignoreUnknownKeys = true }
                val obj = e.response()?.errorBody()?.string()?.let {
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
            val throwable = Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun deleteAccount(): Result<CommonModel> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val user = profileLocalDataSource.getUsers().first()

                profileRemoteDataSource.deleteAccount(
                    user[0].userId
                )
                //preferenceDataStoreHelper.clearAllPreference()
                preferenceDataStoreHelper.removePreference(PreferenceDataStoreConstants.IS_LOGGED_IN)
                preferenceDataStoreHelper.removePreference(PreferenceDataStoreConstants.REFRESH_TOKEN)
                preferenceDataStoreHelper.removePreference(PreferenceDataStoreConstants.ACCESS_TOKEN)
                preferenceDataStoreHelper.removePreference(PreferenceDataStoreConstants.IS_PROFILE_COMPLETE)
                preferenceDataStoreHelper.removePreference(PreferenceDataStoreConstants.IS_Notification_Enable)
                profileLocalDataSource.deleteUsers()

                Result.success(
                    CommonModel(result = "success")
                )

            } catch (e: HttpException) {
                val json = Json { isLenient = true;ignoreUnknownKeys = true }
                val obj = e.response()?.errorBody()?.string()?.let {
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
            val throwable = Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun passwordChange(
        password: String,
        newPassword: String,
    ): Result<CommonModel> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val user = profileLocalDataSource.getUsers().first()

                profileRemoteDataSource.passwordReset(
                    PasswordChangeRequest(
                        password = password,
                        newPassword = newPassword,
                        newConfirmPassword = newPassword,
                        user[0].userId
                    )
                )
                Result.success(
                    CommonModel(result = "success")
                )

            } catch (e: HttpException) {
                val json = Json { isLenient = true;ignoreUnknownKeys = true }
                val obj = e.response()?.errorBody()?.string()?.let {
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
            val throwable = Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun basicInfoUpdate(
        name: String,
        ageCategory: String
    ): Result<UserDataUpdateModel> {
        return if (networkHandler.isNetworkAvailable()) {

            try {
                val user = profileLocalDataSource.getUsers().first()

                val response = profileRemoteDataSource.basicInfoUpdate(
                    user[0].id,
                    BasicInfoRequest(ageCategory = ageCategory, nickname = name)
                ).toUserDataUpdateModel()


                profileLocalDataSource.saveUser(
                    user[0].copy(
                        nickname = response.nickname,
                        ageCategory = response.ageCategory,
                    )
                )

                Result.success(
                    response
                )
            } catch (e: HttpException) {
                var message: String = ""

                e.response()?.errorBody()?.string()?.let {
                    message = "Name Suggestions: $it"
                }

                val throwable =
                    Throwable(message = message ?: "Something went wrong", cause = e.cause)
                Result.failure(throwable)

            } catch (e: Exception) {

                val throwable =
                    Throwable("Something went wrong", cause = e.cause)
                Result.failure(throwable)
            }

        } else {
            val throwable = Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

}