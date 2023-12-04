package com.internetpolice.news_data.repository

import com.internetpolice.core.network.util.NetworkHandler
import com.internetpolice.news_data.dataSource.NewsRemoteDataSource
import com.internetpolice.news_data.mapper.toNewsResponse
import com.internetpolice.news_domain.model.NewsResponse
import com.internetpolice.news_domain.repository.NewsRepository
import org.jsoup.Jsoup


class NewsRepositoryImpl(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val networkHandler: NetworkHandler,
) : NewsRepository {
    override suspend fun getNews(): Result<List<NewsResponse>> {
        return if (networkHandler.isNetworkAvailable()) {
            try {
                val newsDto =
                    newsRemoteDataSource.getNews()

                val newsResponse = newsDto.map { it.toNewsResponse() }.filter { it.tag == "blog" }

                return Result.success(newsResponse)
            } catch (e: Exception) {
                val throwable =
                    Throwable(message = e.message)
                Result.failure(throwable)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }

    override suspend fun getNewsDetails(id: Int): Result<NewsResponse> {
        val s = true
        return if (s) {
            try {
                val newDetailsDto = newsRemoteDataSource.getNewsDetails(id)
                val document = Jsoup.parse(newDetailsDto.content?.rendered ?: "")

                val removeStyle = document.select("style")
                removeStyle.remove()

                val removeScript = document.select("script")
                removeScript.remove()


                newDetailsDto.content!!.rendered =
                    document.select("body").toString()

                val newsDetailsResponse = newDetailsDto.toNewsResponse()

                return Result.success(newsDetailsResponse)
            } catch (e: Exception) {
                val throwable =
                    Throwable(message = e.message)
                Result.failure(throwable)
            }
        } else {
            val throwable =
                Throwable(message = "No internet connection available")
            Result.failure(throwable)
        }
    }


}