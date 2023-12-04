package com.internetpolice.settings_presentation.screens.faq

import com.internetpolice.settings_domain.model.FaqQuestionsResponse

data class FaqState(
    var questionList: List<FaqQuestionsResponse> = emptyList(),
    var isLoading: Boolean = false,
    var content : String = "",
    var id: Int = 0
)
