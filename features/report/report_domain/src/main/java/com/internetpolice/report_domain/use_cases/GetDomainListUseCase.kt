package com.internetpolice.report_domain.use_cases

import com.internetpolice.report_domain.model.DomainModel
import com.internetpolice.report_domain.repository.ReportRepository

class GetDomainListUseCase(private val reportRepository: ReportRepository) {
    suspend operator fun invoke(partialText: String): Result<List<DomainModel>> {
        return reportRepository.getDomainList(partialText)
    }
}