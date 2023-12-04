package com.internetpolice.settings_domain.use_cases

import com.internetpolice.settings_domain.model.ReportProblemModel
import com.internetpolice.settings_domain.repository.SettingsRepository

class ReportProblemUseCase(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(description: String, subject: String): Result<ReportProblemModel> {
        return settingsRepository.reportProblem(description, subject)
    }
}