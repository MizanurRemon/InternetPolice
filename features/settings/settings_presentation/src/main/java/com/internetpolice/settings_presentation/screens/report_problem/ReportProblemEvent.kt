package com.internetpolice.settings_presentation.screens.report_problem

sealed class ReportProblemEvent {
    data class OnDescriptionEnter(val description: String): ReportProblemEvent()
    data class OnSubjectSelect(val subject: String): ReportProblemEvent()
    object OnSubmitClick: ReportProblemEvent()
}
