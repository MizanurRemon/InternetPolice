package com.internetpolice.settings_data.dataSourceImpl

import com.internetpolice.core.network.PrivateApiService
import com.internetpolice.core.network.PublicApiService
import com.internetpolice.core.network.WebsiteApiService
import com.internetpolice.core.network.dto.ReportProblemDto
import com.internetpolice.core.network.dto.SendDescriptionDto
import com.internetpolice.core.network.dto.UserProfile
import com.internetpolice.core.network.dto.WebPageDto
import com.internetpolice.core.network.model.ReportProblemRequest
import com.internetpolice.core.network.model.SendDescriptionRequest
import com.internetpolice.settings_data.dataSource.SettingsRemoteDataSource

class SettingsRemoteDataSourceImpl(
    private val websiteApiService: WebsiteApiService,
    private val privateApiService: PrivateApiService,
    private val publicApiService: PublicApiService,
) : SettingsRemoteDataSource {

    override suspend fun getWebPages(): List<WebPageDto> {

        return websiteApiService.getWebPages()
    }

    override suspend fun reportProblem(reportProblemRequest: ReportProblemRequest): ReportProblemDto {
        return privateApiService.reportProblem(reportProblemRequest)
    }

    override suspend fun sendHelpContactDescription(sendDescriptionRequest: SendDescriptionRequest): SendDescriptionDto {
        return privateApiService.sendHelpContactDescription(sendDescriptionRequest)
    }

    override suspend fun updateProfileLanguage(userId: Int, language: String): UserProfile {
        return privateApiService.updateProfileLanguage(userId, language)
    }

}