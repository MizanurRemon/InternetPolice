package com.internetpolice.settings_domain.use_cases

import com.internetpolice.settings_domain.model.FaqQuestionsResponse
import com.internetpolice.settings_domain.repository.SettingsRepository

class GetFaqList(private val webPageRepository: SettingsRepository) {

    suspend operator fun invoke(): Result<List<FaqQuestionsResponse>> {
        return webPageRepository.getFaqList()
    }
}