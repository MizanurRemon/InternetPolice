package com.internetpolice.settings_domain.use_cases

import com.internetpolice.core.network.dto.UserProfile
import com.internetpolice.settings_domain.repository.SettingsRepository

class UpdateLanguageUseCase(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke(language: String): Result<UserProfile> {
        return settingsRepository.updateProfileLanguage(language = language)
    }
}