package com.internetpolice.core.network.interceptor

import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.PublicApiService
import com.internetpolice.core.network.di.RestConfig
import com.internetpolice.core.network.dto.TokenDto
import com.internetpolice.core.network.model.TokenRequest
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class PrivateInterceptor(private val preferenceDataStoreHelper: PreferenceDataStoreHelper) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()


        val authToken = runBlocking {
            val token = preferenceDataStoreHelper.getFirstPreference(
                PreferenceDataStoreConstants.ACCESS_TOKEN,
                ""
            )
            if (!RestConfig.validateToken(token)) {
                val refreshToken = preferenceDataStoreHelper.getFirstPreference(
                    PreferenceDataStoreConstants.REFRESH_TOKEN, ""
                )
                try {
                    val result = async {
                        getNewToken(refreshToken)
                    }
                    val tokenDto = result.await()

                    if (tokenDto != null) {
                        preferenceDataStoreHelper.putPreference(
                            PreferenceDataStoreConstants.ACCESS_TOKEN,
                            tokenDto.access_token
                        )
                        preferenceDataStoreHelper.putPreference(
                            PreferenceDataStoreConstants.REFRESH_TOKEN,
                            tokenDto.refresh_token
                        )
                    }

                    return@runBlocking tokenDto?.access_token?:""

                } catch (_: Exception) {
                }
            }
            return@runBlocking token

        }

        val newRequest =
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $authToken")
                .addHeader("App-Type", "APP-ANDROID")
                .build()
        return chain.proceed(newRequest)
    }

    private suspend fun getNewToken(
        refreshToken: String,
    ): TokenDto? {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(RestConfig.SERVER_URL_EN)
            .addConverterFactory(
                @OptIn(ExperimentalSerializationApi::class)
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true

                }.asConverterFactory("application/json".toMediaType())
            )
            .client(okHttpClient)
            .build()
        val service = retrofit.create(PublicApiService::class.java)
        return try {
            service.refreshToken(TokenRequest(refreshToken))
        } catch (e: Exception) {
            null
        }
    }
}