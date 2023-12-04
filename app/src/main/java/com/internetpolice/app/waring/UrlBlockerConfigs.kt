package com.internetpolice.app.waring

class SupportedBrowserConfig(var packageName: String, var addressBarId: String)

fun getBrowserPackageNames(): Array<String> {
    val packageNames: MutableList<String> = ArrayList()
    for (config in supportedBrowsers) {
        packageNames.add(config.packageName)
    }
    return packageNames.toTypedArray()
}

/**
 * @return a list of supported browser configs
 * This list could be instead obtained from remote server to support future browser updates without updating an app
 */
val supportedBrowsers: List<SupportedBrowserConfig>
    get() {
        val browsers: MutableList<SupportedBrowserConfig> = ArrayList()
        browsers.add(
            SupportedBrowserConfig(
                "com.android.chrome",
                "com.android.chrome:id/url_bar"
            )
        )
        browsers.add(
            SupportedBrowserConfig(
                "org.mozilla.firefox",
                "org.mozilla.firefox:id/mozac_browser_toolbar_url_view"
            )
        )
        browsers.add(
            SupportedBrowserConfig(
                "com.opera.mini.native",
                "com.opera.mini.native:id/url_field"
            )
        )
        browsers.add(
            SupportedBrowserConfig(
                "com.safaribrowser",
                "com.safaribrowser:id/etUrl"
            )
        )
        browsers.add(
            SupportedBrowserConfig(
                "com.microsoft.emmx",
                "com.microsoft.emmx:id/url_bar"
            )
        )
        return browsers
    }

class SearchEngineConfig(var domainCoreName: String, var searchQuerySyntax: String)

fun isASearchEngineQuery(capturedUrl: String): Boolean {
    return searchEngineConfigList.any {
        capturedUrl.contains(it.domainCoreName) && capturedUrl.contains(
            it.searchQuerySyntax
        )
    }
}

val searchEngineConfigList: List<SearchEngineConfig>
    get() {
        val searchEngineConfigList: MutableList<SearchEngineConfig> = ArrayList()
        searchEngineConfigList.add(
            SearchEngineConfig(
                "google",
                "/search?q="
            )
        )
        searchEngineConfigList.add(
            SearchEngineConfig(
                "bing",
                "/search?q="
            )
        )
        searchEngineConfigList.add(
            SearchEngineConfig(
                "yahoo",
                "/search?p="
            )
        )
        searchEngineConfigList.add(
            SearchEngineConfig(
                "duckduckgo",
                "/?q="
            )
        )
        searchEngineConfigList.add(
            SearchEngineConfig(
                "baidu",
                "/s?wd="
            )
        )
        searchEngineConfigList.add(
            SearchEngineConfig(
                "yandex",
                "/search/?text="
            )
        )
        searchEngineConfigList.add(
            SearchEngineConfig(
                "ask",
                "/web?q="
            )
        )
        return searchEngineConfigList
    }

