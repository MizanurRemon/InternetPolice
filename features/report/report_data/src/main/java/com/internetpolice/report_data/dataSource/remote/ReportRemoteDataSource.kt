package com.internetpolice.report_data.dataSource.remote

import com.internetpolice.core.network.dto.DomainDto
import com.internetpolice.core.network.dto.DomainNameDetailsDto
import com.internetpolice.core.network.model.ReportRequest
import com.internetpolice.core.network.model.ReportResponse


interface ReportRemoteDataSource {
    suspend fun getDomainList(partialText: String): List<DomainDto>
    suspend fun getCategoryTypeList(language: String, type: String): List<String>
    suspend fun reportDomain(reportRequest: ReportRequest): ReportResponse
    suspend fun getDomainDetails(name: String): DomainNameDetailsDto
}