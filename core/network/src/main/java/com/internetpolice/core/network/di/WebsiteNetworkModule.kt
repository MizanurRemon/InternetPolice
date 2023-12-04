package com.internetpolice.core.network.di

import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.WebsiteApiService
import com.internetpolice.core.network.di.qualifier.WebsiteNetwork
import com.internetpolice.core.network.interceptor.WebsiteInterceptor
import com.internetpolice.core.network.util.ResultCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WebsiteNetworkModule {

    @Provides
    @Singleton
    @WebsiteNetwork(TypeEnum.INTERCEPTOR)
    fun provideWebsiteInterceptor(preferenceDataStoreHelper: PreferenceDataStoreHelper): WebsiteInterceptor{
        return WebsiteInterceptor(preferenceDataStoreHelper)
    }

    @Provides
    @Singleton
    @WebsiteNetwork(TypeEnum.OKHTTP)
    fun provideWebsiteOkHttpClient(@WebsiteNetwork(TypeEnum.INTERCEPTOR) websiteInterceptor: WebsiteInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            this.addInterceptor(websiteInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    @WebsiteNetwork(TypeEnum.RETROFIT)
    fun provideWebsiteRetrofit(
        @WebsiteNetwork(TypeEnum.OKHTTP) okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder().baseUrl(RestConfig.ALL_NEWS).callFactory(okHttpClient)
            .addCallAdapterFactory(ResultCallAdapterFactory()).addConverterFactory(
                @OptIn(ExperimentalSerializationApi::class) Json {
                    ignoreUnknownKeys = true;
                    isLenient = true

                }.asConverterFactory("application/json".toMediaType())
            ).build()
    }

    @Provides
    @Singleton
    @WebsiteNetwork(TypeEnum.SERVICE)
    fun provideWebsiteApiService(
        @WebsiteNetwork(TypeEnum.RETROFIT) retrofit: Retrofit,
    ): WebsiteApiService {
        return retrofit.create(WebsiteApiService::class.java)
    }
}