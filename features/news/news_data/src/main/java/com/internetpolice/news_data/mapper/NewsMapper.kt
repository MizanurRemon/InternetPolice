package com.internetpolice.news_data.mapper

import com.internetpolice.core.common.util.DATE_FORMAT
import com.internetpolice.core.common.util.changeDateFormat
import com.internetpolice.core.network.dto.NewsDto
import com.internetpolice.news_domain.model.NewsResponse
import org.jsoup.Jsoup

fun NewsDto.toNewsResponse(): NewsResponse{
    return NewsResponse(
        id = id,
        date = changeDateFormat(
            date,
            "yyyy-MM-dd'T'HH:mm:ss",
            DATE_FORMAT
        ).toString(),
        title = title?.rendered ?: "",
        image = yoast_head_json?.og_image?.get(0)?.url ?: "",
        tag = yoast_head_json?.schema?.graph?.get(0)?.articleSection?.get(0) ?: "",
        authorName = yoast_head_json?.author ?: "",
        description = yoast_head_json?.og_description ?: "",
        content = content?.rendered ?: "",
        newsLink = guid?.rendered ?: ""
    )
}