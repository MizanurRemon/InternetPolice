package com.internetpolice.report_domain.repository


import com.internetpolice.report_domain.model.DomainModel
import com.internetpolice.report_domain.model.DomainNameDetailsModel
import com.internetpolice.report_domain.model.ReportModel
import com.internetpolice.report_domain.model.ReportResponseModel


interface ReportRepository {
    suspend fun getDomainList(partialText: String): Result<List<DomainModel>>
    suspend fun getCategoryTypeList(type: String): Result<List<String>>
    suspend fun reportDomain(reportModel: ReportModel): Result<ReportResponseModel>
    suspend fun getDomainDetails(domainModel: DomainModel): Result<DomainNameDetailsModel>
}