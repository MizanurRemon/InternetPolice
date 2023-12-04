package com.internetpolice.news_domain.repository

import com.internetpolice.news_domain.model.NewsResponse

interface NewsRepository {
    suspend fun getNews(): Result<List<NewsResponse>>
    suspend fun getNewsDetails(id: Int): Result<NewsResponse>
}