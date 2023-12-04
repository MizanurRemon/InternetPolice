package com.internetpolice.report_domain.use_cases

import com.internetpolice.report_domain.repository.ReportRepository

class GetCategoryTypeUseCase(private val reportRepository: ReportRepository) {
    suspend operator fun invoke(type: String): Result<List<String>> {
        return reportRepository.getCategoryTypeList(type)
    }
}