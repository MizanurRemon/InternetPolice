package com.internetpolice.core.network.di

import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.PrivateApiService
import com.internetpolice.core.network.di.qualifier.PrivateNetwork
import com.internetpolice.core.network.interceptor.PrivateInterceptor
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
object PrivateNetworkModule {

    @Provides
    @Singleton
    @PrivateNetwork(TypeEnum.INTERCEPTOR)
    fun provideInterceptor(preferenceDataStoreHelper: PreferenceDataStoreHelper): PrivateInterceptor {
        return PrivateInterceptor(preferenceDataStoreHelper)
    }

    @Provides
    @Singleton
    @PrivateNetwork(TypeEnum.OKHTTP)
    fun provideOkHttpClient(@PrivateNetwork(TypeEnum.INTERCEPTOR) authInterceptor: PrivateInterceptor)
            : OkHttpClient {
        return OkHttpClient.Builder().apply {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            this.addInterceptor(authInterceptor).addInterceptor(httpLoggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS)
        }.build()
    }


    @Singleton
    @Provides
    @PrivateNetwork(TypeEnum.RETROFIT)
    fun provideRetrofit(
        @PrivateNetwork(TypeEnum.OKHTTP) okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder().baseUrl(RestConfig.SERVER_URL_EN).callFactory(okHttpClient)
            .addCallAdapterFactory(ResultCallAdapterFactory()).addConverterFactory(
                @OptIn(ExperimentalSerializationApi::class) Json {
                    ignoreUnknownKeys = true;
                    isLenient = true

                }.asConverterFactory("application/json".toMediaType())
            ).build()
    }


    @Provides
    @Singleton
    @PrivateNetwork(TypeEnum.SERVICE)
    fun providePrivateApiService(
        @PrivateNetwork(TypeEnum.RETROFIT) retrofit: Retrofit,
    ): PrivateApiService {
        return retrofit.create(PrivateApiService::class.java)
    }


}

