package com.internetpolice.report_data.mapper

import com.internetpolice.core.network.dto.DomainDto
import com.internetpolice.core.network.dto.DomainNameDetailsDto
import com.internetpolice.core.network.model.ReportResponse
import com.internetpolice.report_domain.model.DomainModel
import com.internetpolice.report_domain.model.DomainNameDetailsModel
import com.internetpolice.report_domain.model.ReportResponseModel
import com.internetpolice.report_domain.model.VoteDescriptionModel


fun DomainDto.toDomainModel(): DomainModel {
    return DomainModel(
        id = this.id ?: 0,
        domain = this.domain,
        negativeResultCount = this.negativeResultCount,
        trustScore = this.trustScore,
        voteCount = this.voteCount,
    )
}

fun ReportResponse.toReportResponseModel(): ReportResponseModel {
    return ReportResponseModel(
        category = this.category,
        trustTemplateId = this.trustTemplateId,
        voteStatus = this.voteStatus,
        voteType = this.voteType,
        voterId = this.voterId,
        domainStoreId = this.domainStoreId,
        allowedVoteSubmit = this.allowedVoteSubmit,

        )
}

fun DomainNameDetailsDto.toDomainNameDetailsModel(): DomainNameDetailsModel {
    return DomainNameDetailsModel(
        resultMessageKeys = this.resultMessageKeys,
        totalVotes = this.totalVotes,
        voteList = this.voteList.map {
            VoteDescriptionModel(
                avatarImagePath = it.avatarImagePath?:"",
                category = it.category?:"",
                description = it.description?:"",
                id = it.id,
                nickname = it.nickname,
                voteStatus = it.voteStatus?:"",
                voteType = it.voteType?:"",
                rank = it.rank?:""
            )
        },
    )
}



