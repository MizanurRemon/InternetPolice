package com.internetpolice.settings_presentation.screens.report_problem

data class ReportProblemState(
    val description: String = "",
    val isError: Boolean = false,
    val isShowDialog: Boolean = false,
    val subject : String = ""

)
