package com.internetpolice.report_domain.use_cases


data class ReportUseCases(
    val getCategoryTypeUseCase: GetCategoryTypeUseCase,
    val reportDomainUseCase: ReportDomainUseCase,
    val getDomainUseCase: GetDomainListUseCase,
    val getDomainDetailsUseCase: GetDomainDetailsUseCase
)