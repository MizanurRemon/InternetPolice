package com.internetpolice.report_domain.use_cases

import com.internetpolice.report_domain.model.ReportModel
import com.internetpolice.report_domain.model.ReportResponseModel
import com.internetpolice.report_domain.repository.ReportRepository

class ReportDomainUseCase(private val reportRepository: ReportRepository) {
    suspend operator fun invoke(reportModel: ReportModel): Result<ReportResponseModel> {
        return reportRepository.reportDomain(reportModel)
    }
}