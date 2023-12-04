package com.internetpolice.settings_presentation.screens.language

import com.internetpolice.core.designsystem.components.LanguageResponse
import com.internetpolice.core.designsystem.languageList

data class LanguageState(
    var isShowDialog: Boolean = false,
    var language: LanguageResponse = languageList[0],
)
