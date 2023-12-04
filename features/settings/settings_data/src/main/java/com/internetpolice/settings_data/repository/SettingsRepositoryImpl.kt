package com.internetpolice.settings_data.repository

import com.internetpolice.core.common.util.DEFAULT_LANGUAGE_TAG
import com.internetpolice.core.common.util.LanguageTagEnum
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.dto.UserProfile
import com.internetpolice.core.network.model.CommonErrorModel
import com.internetpolice.core.network.model.ReportProblemRequest
import com.internetpolice.core.network.model.SendDescriptionRequest
import com.internetpolice.core.network.util.NetworkHandler
import com.internetpolice.settings_data.dataSource.SettingsLocalDataSource
import com.internetpolice.settings_data.dataSource.SettingsRemoteDataSource
import com.internetpolice.settings_data.mapper.toHelpContactResponse
import com.internetpolice.settings_data.mapper.toReportProblemModel
import com.internetpolice.settings_data.mapper.toWebPageResponse
import com.internetpolice.settings_domain.model.AboutUsResponse
import com.internetpolice.settings_domain.model.FaqQuestionsResponse
import com.internetpolice.settings_domain.model.HelpContactResponse
import com.internetpolice.settings_domain.model.ReportProblemModel
import com.internetpolice.settings_domain.repository.SettingsRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.HttpException

class SettingsRepositoryImpl(
    private val settingsRemoteDataSource: SettingsRemoteDataSource,
    private val settingsLocalDataSource: SettingsLocalDataSource,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private var networkHandler: NetworkHandler,
) : SettingsRepository {
    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun getFaqList(): Result<List<FaqQuestionsResponse>> {

        return if (networkHandler.isNetworkAvailable()) {
            try {
                val tag = preferenceDataStoreHelper.getFirstPreference(
                    PreferenceDataStoreConstants.LANGUAGE_TAG,
                    DEFAULT_LANGUAGE_TAG
                )

                val faqUrl =
                    if (tag == DEFAULT_LANGUAGE_TAG) "https://internetpolitie.nl/#faq" else "https://internetpolice.com/#faq"
                val questionList: MutableList<FaqQuestionsResponse> =
                    emptyList<FaqQuestionsResponse>().toMutableList()


                return GlobalScope.async {
                    val document = Jsoup.connect(faqUrl).get()
                    val divs = document.getElementsByClass("elementor-accordion-item")

                    for (i in divs.indices) {
                        val question =
                            divs[i].getElementsByClass("elementor-accordion-title").text()
                        val answer = divs[i].getElementsByClass("elementor-tab-content").text()
                        questionList.add(FaqQuestionsResponse(question, answer))

                    }


                    return@async Result.success(questionList)
                }.await()
            } catch (e: Exception) {
                val throwable =
                    Throwable(message = e.message)
                Result.failure(throwable)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun getAboutUs(): Result<AboutUsResponse> {

        return if (networkHandler.isNetworkAvailable()) {
            try {

                val webPageDto =
                    settingsRemoteDataSource.getWebPages()
                val newsResponse = webPageDto.map { it.toWebPageResponse() }

                val document: Document = Jsoup.parse(newsResponse[9].content)
                val divs =
                    document.getElementsByClass("elementor-element-bf6ae6d").text() + "\n\n" +
                            document.getElementsByClass("elementor-element-035393f").text()

                return Result.success(AboutUsResponse(divs))
            } catch (e: Exception) {
                val throwable =
                    Throwable(message = e.message)
                Result.failure(throwable)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }


    override suspend fun reportProblem(
        description: String,
        subject: String
    ): Result<ReportProblemModel> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val user = settingsLocalDataSource.getUsers().first()

                val response = settingsRemoteDataSource.reportProblem(
                    ReportProblemRequest(
                        userId = user[0].userId,
                        description = description,
                        issueCategory = subject
                    )
                )

                Result.success(response.toReportProblemModel())
            } catch (e: HttpException) {
                val json =
                    Json { isLenient = true;ignoreUnknownKeys = true }
                val obj = e.response()?.errorBody()?.string()
                    ?.let {
                        json.decodeFromString<CommonErrorModel>(it)
                    }

                val message = if (obj?.description!!.isEmpty()) {
                    obj?.error?.let { it }.toString()
                } else {
                    obj?.description!![0]?.let { it }.toString()
                }

                val throwable =
                    Throwable(message = message, cause = e.cause)
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

    override suspend fun sendHelpContactDescription(description: String): Result<HelpContactResponse> {
        return if (networkHandler.isNetworkAvailable()) {

            try {

                val tag = preferenceDataStoreHelper.getFirstPreference(
                    PreferenceDataStoreConstants.LANGUAGE_TAG,
                    DEFAULT_LANGUAGE_TAG
                )

                val user = settingsLocalDataSource.getUsers().first()
                val response = settingsRemoteDataSource.sendHelpContactDescription(
                    SendDescriptionRequest(
                        nickname = user[0].nickname,
                        email = user[0].email,
                        description = description,
                        language = LanguageTagEnum.values().first { it.tag == tag }.name
                    )

                )

                Result.success(response.toHelpContactResponse())
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
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun updateProfileLanguage(language: String): Result<UserProfile> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val user = settingsLocalDataSource.getUsers().first()

                val response = settingsRemoteDataSource.updateProfileLanguage(
                    userId = user[0].userId,
                    language = language
                )

                Result.success(response)
            } catch (e: HttpException) {
                val json =
                    Json { isLenient = true;ignoreUnknownKeys = true }
                val obj = e.response()?.errorBody()?.string()
                    ?.let {
                        json.decodeFromString<CommonErrorModel>(it)
                    }

                val message = if (obj?.description!!.isEmpty()) {
                    obj?.error?.let { it }.toString()
                } else {
                    obj?.description!![0]?.let { it }.toString()
                }

                val throwable =
                    Throwable(message = message, cause = e.cause)
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


}