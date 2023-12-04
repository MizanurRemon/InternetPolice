package com.internetpolice.core.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


object PreferenceDataStoreConstants {
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val IS_PROFILE_COMPLETE = booleanPreferencesKey("is_profile_complete")
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val LANGUAGE_TAG = stringPreferencesKey("language_tag")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val IS_Notification_Enable = booleanPreferencesKey("is_notification_enable")
}