package com.internetpolice.core.common.util

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Base64
import android.util.Log
import com.google.common.net.InternetDomainName
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern


fun changeDateFormat(dateTime: String, oldFormat: String, newFormat: String): String? {
    val input = SimpleDateFormat(oldFormat)
    val output = SimpleDateFormat(newFormat)
    try {
        val getAbbreviate = input.parse(dateTime)    // parse input
        return output.format(getAbbreviate)    // format output
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return null
}

fun getDomainName(url: String): String {
    return try {
        val modifiedUrl =
            if (!url.startsWith("http://") && !url.startsWith("https://")) "http://${url}" else url
        val uri = URL(modifiedUrl)
        uri.host
    } catch (e: Exception) {
        url
    }
}

fun getUrlWithProtocol(url: String): String {
    return try {
        return if (!url.startsWith("http://") && !url.startsWith("https://")) "http://${url}" else url
    } catch (e: Exception) {
        url
    }
}

fun btoa(input: String): String {
    val bytes = input.toByteArray()
    return Base64.encodeToString(bytes, Base64.NO_PADDING).trim()
}

fun getFormattedDate(dateTime: Long, format: String): String {
    val output = SimpleDateFormat(format, Locale.ENGLISH)
    try {
        return output.format(Date(dateTime))    // format output
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun domainNameWithoutExt(domain: String): String {
    var domainName = ""
    val pattern: Pattern = Pattern.compile("^(.*?)\\.(\\w+)$")
    val matcher: Matcher = pattern.matcher(domain)
    if (matcher.matches()) {
        domainName = matcher.group(1)!!
    }
    return domainName
}

fun getHostName(domain: String): String {


    val url = InternetDomainName.from(domain).topPrivateDomain().toString()
    val it: InternetDomainName? = InternetDomainName.from(url)
    val domainName = it!!.publicSuffix().toString()

    return url.replace(".$domainName".toRegex(), "")
}

fun updateResourcesLanguage(language: String, context: Context): Resources {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val configuration = Configuration(context.resources.configuration)
    configuration.setLocale(locale)

    return context.createConfigurationContext(configuration).resources
}

fun updateSharedPreferences(language: String, context: Context) {
    val sharedPreferences = context.getSharedPreferences(
        APP_SETTINGS,
        AccessibilityService.MODE_PRIVATE
    )
    with(sharedPreferences.edit()) {
        putString(LANGUAGE_KEY, language)
        apply()
    }
}

