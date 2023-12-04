package com.internetpolice.core.network.di

import org.json.JSONObject
import java.util.*

object RestConfig {
    const val LOCAL_URL = ""
    const val ALL_NEWS = "https://internetpolice.com/"
    const val DUTCH_NEWS = "https://internetpolitie.nl/"

    private const val SERVER_URL_NL_STAGE = "https://api-staging.internetpolitie.nl/"
    private const val SERVER_URL_EN_STAGE = "https://api-staging.internetpolitie.nl/"
    private const val SERVER_TEST_URL = "https://api-test.internetpolice.com/"

    private const val SERVER_URL_NL_PRODUCTION= "https://api-prod.internetpolice.com/"
    private const val SERVER_URL_EN_PRODUCTION= "https://api-prod.internetpolice.com/"

    const val SERVER_URL_NL = SERVER_TEST_URL
    const val SERVER_URL_EN = SERVER_TEST_URL

    const val SERVER_URL_DOMAIN_DETAILS = "https://dcs.webest.cf/"
    private const val SERVER_URL_CACHE_OLD = "https://dcs.webest.cf/"
    private const val SERVER_URL_CACHE_STAGING = "https://dcs-staging.internetpolitie.nl/"
    private const val SERVER_URL_CACHE_TEST = "https://dcs-test.internetpolice.com/"

    const val SERVER_URL_CACHE = SERVER_URL_CACHE_TEST
    const val SERVER_URL_FOR_SCRAPPING = SERVER_TEST_URL
    fun validateToken(token: String): Boolean {
        try {
            val parts = token.split(".")
            val decode = String(Base64.getUrlDecoder().decode(parts[1]))
            val payload = JSONObject(decode)

            if (payload.getLong("exp") > (System.currentTimeMillis() / 1000)) {
                return true
            }
        } catch (_: Exception) {
        }
        return false
    }
}