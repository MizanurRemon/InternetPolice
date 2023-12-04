package com.internetpolice.news_domain.use_cases

import android.util.Log
import com.internetpolice.news_domain.model.NewsResponse
import com.internetpolice.news_domain.repository.NewsRepository

class GetNewsData(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(): Result<List<NewsResponse>> {
        return newsRepository.getNews()
    }
}