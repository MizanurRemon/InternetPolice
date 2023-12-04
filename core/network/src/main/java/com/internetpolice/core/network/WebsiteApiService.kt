package com.internetpolice.core.network

import com.internetpolice.core.network.dto.WebPageDto
import com.internetpolice.core.network.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WebsiteApiService {
    @GET("wp-json/wp/v2/posts")
    suspend fun getNews(): List<NewsDto>

    @GET("wp-json/wp/v2/posts/{id}")
    suspend fun getNewsDetails(
        @Path("id") id: Int
    ): NewsDto

    @GET("wp-json/wp/v2/pages")
    suspend fun getWebPages(): List<WebPageDto>

}