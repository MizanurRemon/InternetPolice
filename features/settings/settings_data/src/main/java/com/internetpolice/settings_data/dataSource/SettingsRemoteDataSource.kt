package com.internetpolice.settings_data.dataSource

import com.internetpolice.core.network.dto.ReportProblemDto
import com.internetpolice.core.network.dto.SendDescriptionDto
import com.internetpolice.core.network.dto.UserProfile
import com.internetpolice.core.network.dto.WebPageDto
import com.internetpolice.core.network.model.ReportProblemRequest
import com.internetpolice.core.network.model.SendDescriptionRequest

interface SettingsRemoteDataSource {

    suspend fun getWebPages(): List<WebPageDto>
    suspend fun reportProblem(reportProblemRequest: ReportProblemRequest): ReportProblemDto
    suspend fun sendHelpContactDescription(sendDescriptionRequest: SendDescriptionRequest): SendDescriptionDto
    suspend fun updateProfileLanguage(userId: Int, language: String): UserProfile

}