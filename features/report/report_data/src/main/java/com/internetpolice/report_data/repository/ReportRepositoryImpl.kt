package com.internetpolice.report_data.repository

import com.internetpolice.core.common.util.DEFAULT_LANGUAGE_TAG
import com.internetpolice.core.common.util.ENGLISH_TAG
import com.internetpolice.core.common.util.LanguageTagEnum
import com.internetpolice.core.common.util.btoa
import com.internetpolice.core.common.util.getDomainName
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.dto.DomainDetailsDto
import com.internetpolice.core.network.model.CommonErrorModel
import com.internetpolice.core.network.model.ReportRequest
import com.internetpolice.core.network.service_retrofit.RetrofitInstance
import com.internetpolice.core.network.util.NetworkHandler
import com.internetpolice.report_data.dataSource.local.ReportLocalDataSource
import com.internetpolice.report_data.dataSource.remote.ReportRemoteDataSource
import com.internetpolice.report_data.mapper.toDomainModel
import com.internetpolice.report_data.mapper.toDomainNameDetailsModel
import com.internetpolice.report_data.mapper.toReportResponseModel
import com.internetpolice.report_domain.model.DomainModel
import com.internetpolice.report_domain.model.DomainNameDetailsModel
import com.internetpolice.report_domain.model.ReportModel
import com.internetpolice.report_domain.model.ReportResponseModel
import com.internetpolice.report_domain.repository.ReportRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.HttpException


class ReportRepositoryImpl(
    private val reportRemoteDataSource: ReportRemoteDataSource,
    private val reportLocalDataSource: ReportLocalDataSource,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private val networkHandler: NetworkHandler,
) : ReportRepository {
    override suspend fun getDomainList(partialText: String): Result<List<DomainModel>> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                Result.success(reportRemoteDataSource.getDomainList(partialText)
                    .map { it.toDomainModel() })

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

    override suspend fun getCategoryTypeList(type: String): Result<List<String>> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val tag = preferenceDataStoreHelper.getFirstPreference(
                    PreferenceDataStoreConstants.LANGUAGE_TAG, DEFAULT_LANGUAGE_TAG
                )
                Result.success(
                    RetrofitInstance.getApiInstance().getCategoryTypeList(
                        LanguageTagEnum.values().first { it.tag == tag }.name,
                        type
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

    override suspend fun reportDomain(reportModel: ReportModel): Result<ReportResponseModel> {
        return if (networkHandler.isNetworkAvailable()) {
            try {

                var report = reportModel

                if (report.domainStoreId == -1) {
                    getDomainName(reportModel.domainName)
                    val response =
                        reportRemoteDataSource.getDomainList(getDomainName(report.domainName))
                            .map { it.toDomainModel() }
                    if (response.isNotEmpty()) {
                        report = report.copy(
                            domainStoreId = response[0].id, domainName = response[0].domain
                        )
                    }

                }
                if (report.domainStoreId == 0) {
                    val domainDetailsDto = getDomainDetails(report.domainName)
                    if (domainDetailsDto != null) report =
                        report.copy(domainStoreId = domainDetailsDto.id)
                }


                val user = reportLocalDataSource.getUsers().first()
                Result.success(
                    reportRemoteDataSource.reportDomain(
                        ReportRequest(
                            category = report.category,
                            trustTemplateId = report.trustTemplateId,
                            voteStatus = report.voteStatus,
                            voteType = report.voteType.uppercase(),
                            voterId = user[0].userId,
                            domainStoreId = report.domainStoreId,
                            description = report.description ?: ""
                        )
                    ).toReportResponseModel()
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

    override suspend fun getDomainDetails(domainModel: DomainModel): Result<DomainNameDetailsModel> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                Result.success(
                    reportRemoteDataSource.getDomainDetails(btoa(domainModel.domain))
                        .toDomainNameDetailsModel()
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

    private suspend fun getDomainDetails(domainName: String): DomainDetailsDto? {
        val encodeUrl = btoa(domainName)
        return try {
            RetrofitInstance.getApiInstance().domainDetails(encodeUrl)
        } catch (e: Exception) {
            null
        }
    }
}