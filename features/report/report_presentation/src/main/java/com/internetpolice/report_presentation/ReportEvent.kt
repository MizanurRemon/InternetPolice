package com.internetpolice.report_presentation

import com.internetpolice.report_domain.model.DomainModel

sealed class ReportEvent {
    data class OnSearchChange(val partialText: String) : ReportEvent()
    data class OnDescriptionInput(val description: String) : ReportEvent()
    data class OnSelectedDomain(val domainModel: DomainModel) : ReportEvent()
    data class OnSelectedReportType(val type: String) : ReportEvent()
    data class OnSelectCategory(val category: String) : ReportEvent()
    object OnSearchResult : ReportEvent()
     object OnReportTypeResult : ReportEvent()
     object OnSubmitReport : ReportEvent()
    object OnBack: ReportEvent()

}
