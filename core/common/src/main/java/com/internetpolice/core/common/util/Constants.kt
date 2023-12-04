package com.internetpolice.core.common.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Browser
import android.text.Html
import androidx.core.text.HtmlCompat
import com.internetpolice.core.common.R

const val INTERNAL_ERROR = -1
const val EMAIL_REGEX =
    "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"
const val URL_REGEX =
    "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$"
const val ENGLISH_TAG = "en"
const val DEFAULT_LANGUAGE_TAG = "nl"
const val FAV_ICON_PREFIX_URL = "https://www.google.com/s2/favicons?sz=256&domain="
const val DATE_FORMAT = "dd/MM/yyyy"

const val APP_SETTINGS = "app_settings"
const val LANGUAGE_KEY = "language_key"
const val TERMS_SERVICE_URL = "https://internetpolice.com/general-terms-conditions/"
const val COMMUNITY_URL = "https://internetpolitie.nl/forums/"
const val PROFILE_PANEL_TEST = "https://profile-panel-test.internetpolice.com/"
const val PROFILE_PANEL_STAGING = "https://profile-panel-staging.internetpolice.com/"
const val WARNING_URL = PROFILE_PANEL_TEST
const val REDIRECT_URL = WARNING_URL + "website-warning"

const val TWITTER_URL = "https://twitter.com/IPoliceNL"
const val LINKEDIN_URL= "https://www.linkedin.com/company/internet-politie-b-v/"
const val FACEBOOK_URL= "https://www.facebook.com/InternetPolitie"
const val INSTAGRAM_URL = "https://www.instagram.com/internet_politie"

val AGE_LIST = listOf("-18", "18-34", "35-60", "60+")

val REPORT_SUBJECT_LIST = mutableListOf(
    R.string.problem_with_account,
    R.string.bug_or_issue_with_the_application,
    R.string.other_problem
)

enum class LanguageTagEnum(val tag: String) {
    English("en"),
    Dutch("nl"),
    German("de"),
    Spanish("es"),
    French("fr"),
    Italiano("it"),
}

enum class LanguageListEnum(val lang: String) {
    English("English"),
    Nederlands("Dutch"),
    Deutsch("German"),
    Español("Spanish"),
    Français("French"),
    Italiano("Italiano"),
}


fun intent(browserPackage: String, redirectUrl: String): Intent {
    return try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(addPrefixToUrl(redirectUrl)))
        intent.setPackage(browserPackage)
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, browserPackage)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        )
        intent
    } catch (e: ActivityNotFoundException) {
        // the expected browser is not installed
        Intent(Intent.ACTION_VIEW, Uri.parse(addPrefixToUrl(redirectUrl)))
    }
}

fun addPrefixToUrl(url: String): String {
    var newUrl = url
    if (!newUrl.startsWith("https://")) {
        newUrl = "https://$newUrl"
    }
    return newUrl
}

fun openExternalLink(url: String, context: Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {

    }
}

fun openMailBox(mail: String, subject: String, context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, "")
    intent.data = Uri.parse(mail)

    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    try {
        context.startActivity(intent)
    } catch (e: Exception) {

    }
}

enum class SpecialTagsFromServer(val value: Int) {
    COMPLETE_PROFILE(R.string.complete_profile), CAST_VOTE(R.string.cast_vote), VOTE_VERIFIED_SUSPICIOUS(
        R.string.vote_verified_suspicious
    ),
    VOTE_VERIFIED_MALICIOUS(R.string.vote_verified_malicious), VOTE_SUBMIT_DESC_INFO(R.string.vote_submit_desc_info), INVITE_USER(
        R.string.invite_user
    ),
    INVITE_USER_DETECTIVE(R.string.invite_user_detective), SUGGEST_PARTNER(R.string.suggest_partner), SUGGEST_PARTNER_APPROVED(
        R.string.suggest_partner_approved
    ),
    POSTING(R.string.posting), POSTING_LIKE(R.string.posting_like), POSTING_LIKE_RECEIVE(R.string.posting_like_receive), ANSWERING_QUESTION(
        R.string.answering_question
    ),
    SOLVING_ISSUE(R.string.solving_issue), BEST_ANSWER(R.string.best_answer), ENDORSE_ANSWER(R.string.endorse_answer), ENDORSE_SOLUTION(
        R.string.endorse_solution
    ),
    ENDORSED_BY_SOMEONE(R.string.endorsed_by_someone), FOUND_NEW_THREAT(R.string.found_new_threat), VOTE_MAX_REACHED(
        R.string.vote_max_reached
    ),
    RESULT_PHISHUNTINFO(R.string.phishuntInfo), RESULT_WEBWINKELKEURINFO(R.string.webwinkelkeurInfo), RESULT_DOMAINTOOLSNORISK(
        R.string.domainToolsNoRisk
    ),
    RESULT_DOMAINTOOLSLOWRISK(R.string.domainToolsLowRisk), RESULT_DOMAINTOOLSMODERATERISK(R.string.domainToolsModerateRisk), RESULT_DOMAINTOOLSHIGHRISK(
        R.string.domainToolsHighRisk
    ),
    RESULT_DOMAINTOOLSVERYHIGHRISK(R.string.domainToolsVeryHighRisk), RESULT_AA419INFO(R.string.aa419Info)
}

fun shareStringWithOthers(text: String, context: Context) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "Choose Option")
    context.startActivity(shareIntent)
}

fun convertHtmlToText(text: String): String {
    return HtmlCompat.fromHtml(text, 0).toString()
}




