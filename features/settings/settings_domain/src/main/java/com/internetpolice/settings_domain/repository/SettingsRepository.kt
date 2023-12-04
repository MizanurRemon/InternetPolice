package com.internetpolice.settings_domain.repository

import com.internetpolice.core.network.dto.UserProfile
import com.internetpolice.settings_domain.model.AboutUsResponse
import com.internetpolice.settings_domain.model.FaqQuestionsResponse
import com.internetpolice.settings_domain.model.HelpContactResponse
import com.internetpolice.settings_domain.model.ReportProblemModel

interface SettingsRepository {

    suspend fun getFaqList(): Result<List<FaqQuestionsResponse>>
    suspend fun getAboutUs(): Result<AboutUsResponse>
    suspend fun reportProblem(description: String, subject: String): Result<ReportProblemModel>
    suspend fun sendHelpContactDescription(description: String): Result<HelpContactResponse>
    suspend fun updateProfileLanguage(language: String): Result<UserProfile>
}