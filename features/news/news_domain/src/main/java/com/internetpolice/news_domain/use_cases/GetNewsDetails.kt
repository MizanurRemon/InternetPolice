package com.internetpolice.news_domain.use_cases

import com.internetpolice.news_domain.model.NewsResponse
import com.internetpolice.news_domain.repository.NewsRepository

class GetNewsDetails(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(id: Int): Result<NewsResponse> {
        return newsRepository.getNewsDetails(id)
    }
}