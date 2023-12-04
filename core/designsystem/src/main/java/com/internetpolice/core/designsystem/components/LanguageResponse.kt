package com.internetpolice.core.designsystem.components

import com.internetpolice.core.common.util.LanguageListEnum

data class LanguageResponse(
    val title: LanguageListEnum,
    val image: Int,
    val tag: String,
    var selected: Boolean = false
)


