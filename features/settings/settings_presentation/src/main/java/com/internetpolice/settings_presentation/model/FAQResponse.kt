package com.internetpolice.settings_presentation.model

import com.internetpolice.settings_domain.model.FaqQuestionsResponse

data class FAQResponse(
    val title: String,
    val questions: List<FaqQuestionsResponse>
)
