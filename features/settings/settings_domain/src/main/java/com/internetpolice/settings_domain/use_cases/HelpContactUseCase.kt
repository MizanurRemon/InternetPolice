package com.internetpolice.settings_domain.use_cases

import com.internetpolice.settings_domain.model.HelpContactResponse
import com.internetpolice.settings_domain.repository.SettingsRepository

class HelpContactUseCase(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(description: String): Result<HelpContactResponse> {
        return settingsRepository.sendHelpContactDescription(description)
    }
}