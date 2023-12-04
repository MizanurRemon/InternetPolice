package com.internetpolice.settings_domain.use_cases

import com.internetpolice.settings_domain.model.AboutUsResponse
import com.internetpolice.settings_domain.repository.SettingsRepository

class GetAboutUs(private val webPageRepository: SettingsRepository) {
    suspend operator fun invoke(): Result<AboutUsResponse> {
        return webPageRepository.getAboutUs()
    }
}