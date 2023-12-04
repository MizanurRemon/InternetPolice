package com.internetpolice.settings_presentation.screens.HelpContact

data class HelpContactState(
    var isShowDialog: Boolean = false,
    var text: String = "",
    var mailTo: String = "",
    var description: String = "",
    val isError: Boolean = false,
)
