package com.internetpolice.news_data.dataSource

import com.internetpolice.core.network.dto.NewsDto

interface NewsRemoteDataSource {
    suspend fun getNews(): List<NewsDto>

    suspend fun getNewsDetails(id: Int): NewsDto
}