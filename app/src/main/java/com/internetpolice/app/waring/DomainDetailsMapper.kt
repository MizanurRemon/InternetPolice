package com.internetpolice.app.waring

import com.internetpolice.core.network.dto.DomainDetailsDto
import com.internetpolice.report_domain.model.DomainModel

fun DomainDetailsDto.toDomainModel(): DomainModel {
    return DomainModel(
        domain = domain ?: "",
        id = id,
        negativeResultCount = 0,
        trustScore = trustScore,
        voteCount = 0
    )
}