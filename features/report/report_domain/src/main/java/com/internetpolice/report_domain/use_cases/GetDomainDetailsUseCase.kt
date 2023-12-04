package com.internetpolice.report_domain.use_cases

import com.internetpolice.report_domain.model.DomainModel
import com.internetpolice.report_domain.model.DomainNameDetailsModel
import com.internetpolice.report_domain.repository.ReportRepository

class GetDomainDetailsUseCase(private val reportRepository: ReportRepository) {
    suspend operator fun invoke(domainModel: DomainModel): Result<DomainNameDetailsModel> {
        return reportRepository.getDomainDetails(domainModel)
    }
}