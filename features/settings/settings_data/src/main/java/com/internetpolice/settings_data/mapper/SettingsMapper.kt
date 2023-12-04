package com.internetpolice.settings_data.mapper

import com.internetpolice.core.network.dto.ReportProblemDto
import com.internetpolice.core.network.dto.SendDescriptionDto
import com.internetpolice.core.network.dto.WebPageDto
import com.internetpolice.settings_domain.model.HelpContactResponse
import com.internetpolice.settings_domain.model.ReportProblemModel
import com.internetpolice.settings_domain.model.WebPageResponse

fun WebPageDto.toWebPageResponse(): WebPageResponse {
    return WebPageResponse(
        id = id,
        content = content.rendered
    )
}

fun ReportProblemDto.toReportProblemModel(): ReportProblemModel {
    return ReportProblemModel(
        id = this.id,
        userId = this.userId,
        description = this.description
    )
}

fun SendDescriptionDto.toHelpContactResponse(): HelpContactResponse {
    return HelpContactResponse(text = message, statusCode = "")
}