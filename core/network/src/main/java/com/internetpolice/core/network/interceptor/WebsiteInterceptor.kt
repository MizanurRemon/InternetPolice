package com.internetpolice.core.network.interceptor

import com.internetpolice.core.common.util.DEFAULT_LANGUAGE_TAG
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.di.RestConfig
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

class WebsiteInterceptor(private val preferenceDataStoreHelper: PreferenceDataStoreHelper):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val tag = runBlocking {
            preferenceDataStoreHelper.getFirstPreference(
                PreferenceDataStoreConstants.LANGUAGE_TAG,
                DEFAULT_LANGUAGE_TAG
            )
        }

        val newsUrl = if (tag == DEFAULT_LANGUAGE_TAG) RestConfig.DUTCH_NEWS else RestConfig.DUTCH_NEWS//RestConfig.ALL_NEWS

        val originalUrl: HttpUrl = originalRequest.url

        val newUrl: HttpUrl = newsUrl.toHttpUrlOrNull()!!
            .newBuilder()
            .addPathSegments(originalUrl.encodedPath)
            .build()

        val newRequest =
            originalRequest.newBuilder()
                .url(newUrl)
                .addHeader("App-Type", "APP-ANDROID")
                .build()
        return chain.proceed(newRequest)
    }
}