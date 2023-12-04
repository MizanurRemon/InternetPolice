package com.internetpolice.news_data.dataSourceImpl

import com.internetpolice.core.network.WebsiteApiService
import com.internetpolice.core.network.dto.NewsDto
import com.internetpolice.news_data.dataSource.NewsRemoteDataSource

class NewsRemoteDataSourceImpl(private val appApiService: WebsiteApiService) : NewsRemoteDataSource {
    override suspend fun getNews(): List<NewsDto> {
        return appApiService.getNews()
    }

    override suspend fun getNewsDetails(id: Int): NewsDto {
        return appApiService.getNewsDetails(id)
    }
}