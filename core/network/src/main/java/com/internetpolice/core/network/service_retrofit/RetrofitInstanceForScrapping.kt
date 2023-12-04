package com.internetpolice.core.network.service_retrofit

import com.internetpolice.core.network.PrivateApiService
import com.internetpolice.core.network.di.RestConfig
import com.internetpolice.core.network.util.ResultCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RetrofitInstanceForScrapping {
    private fun okHttpClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .addHeader("App-Type", "APP-ANDROID")
                .build()
        )
    }.apply {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        this.addInterceptor(httpLoggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
    }.build()

    private fun retrofit(): Retrofit =
        Retrofit.Builder().baseUrl(RestConfig.SERVER_URL_FOR_SCRAPPING)
            .callFactory(okHttpClient())
            .addCallAdapterFactory(ResultCallAdapterFactory()).addConverterFactory(
                @OptIn(ExperimentalSerializationApi::class) Json {
                    ignoreUnknownKeys = true
                    isLenient = true

                }.asConverterFactory("application/json".toMediaType())
            ).build()


    fun getApiInstance(): PrivateApiService =
        retrofit().create(PrivateApiService::class.java)
}