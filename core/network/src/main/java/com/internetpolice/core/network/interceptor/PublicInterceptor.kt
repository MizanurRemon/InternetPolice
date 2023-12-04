package com.internetpolice.core.network.interceptor

import android.util.Log
import com.internetpolice.core.common.util.DEFAULT_LANGUAGE_TAG
import com.internetpolice.core.common.util.ENGLISH_TAG
import com.internetpolice.core.common.util.LanguageTagEnum
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.di.RestConfig
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response


class PublicInterceptor(private val preferenceDataStoreHelper: PreferenceDataStoreHelper) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        var tag = runBlocking {
            preferenceDataStoreHelper.getFirstPreference(
                PreferenceDataStoreConstants.LANGUAGE_TAG,
                DEFAULT_LANGUAGE_TAG
            )
        }

        val tagUrl = if (tag == DEFAULT_LANGUAGE_TAG) RestConfig.SERVER_URL_NL else RestConfig.SERVER_URL_EN

        val originalUrl: HttpUrl = originalRequest.url

        val newUrl: HttpUrl = tagUrl.toHttpUrlOrNull()!!
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