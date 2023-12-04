package com.internetpolice.report_data.dataSourceImpl.remote

import com.internetpolice.core.network.PrivateApiService
import com.internetpolice.core.network.PublicApiService
import com.internetpolice.core.network.dto.DomainDto
import com.internetpolice.core.network.dto.DomainNameDetailsDto
import com.internetpolice.core.network.model.ReportRequest
import com.internetpolice.core.network.model.ReportResponse
import com.internetpolice.report_data.dataSource.remote.ReportRemoteDataSource


class ReportRemoteDataSourceImpl(
    private val publicApiService: PublicApiService,
    private val privateApiService: PrivateApiService,
) : ReportRemoteDataSource {
    override suspend fun getDomainList(partialText: String): List<DomainDto> {
        return publicApiService.getDomainList(partialText)
    }

    override suspend fun getCategoryTypeList(language: String, type: String): List<String> {
        return privateApiService.getCategoryTypeList(language, type)
    }

    override suspend fun reportDomain(reportRequest: ReportRequest): ReportResponse {
        return privateApiService.reportDomain(reportRequest)
    }

    override suspend fun getDomainDetails(name: String): DomainNameDetailsDto {
        return publicApiService.getDomainDetails(name)
    }

}