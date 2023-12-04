package com.internetpolice.report_presentation

import com.internetpolice.report_domain.model.DomainModel
import com.internetpolice.report_domain.model.VoteDescriptionModel
import com.internetpolice.report_presentation.report.ReportCategory
import com.internetpolice.report_presentation.report.reportCategories

data class ReportState(
    val selectedDomain: DomainModel? = null,
    val selectedDomainId: Int = -1,
    val reportType: String = "",
    val domainList: List<DomainModel> = emptyList(),
    val searchText: String = "www.",
    val category: String = "",
    val voteStatus: String = "NEW",
    val reportTypeList: List<ReportCategory> = reportCategories,
    val isSearching: Boolean = false,
    val isShowLoading: Boolean = false,
    val description: String? = "",
    val resultMessageKeys: List<String> = emptyList(),
    val totalVotes: Int = 0,
    val voteList: List<VoteDescriptionModel> = emptyList(),
)
