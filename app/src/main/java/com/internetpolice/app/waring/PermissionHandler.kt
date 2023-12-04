package com.internetpolice.app.waring

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.view.accessibility.AccessibilityManager
import androidx.core.content.ContextCompat.getSystemService

fun isAccessibilitySettingsOn(
    context: Context,
    service: Class<out AccessibilityService>
): Boolean {
    val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices =
        am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
    for (enabledService in enabledServices) {
        val enabledServiceInfo = enabledService.resolveInfo.serviceInfo
        if (enabledServiceInfo.packageName == context.packageName && enabledServiceInfo.name == service.name) {
            return true
        }
    }
    return false
}

fun hasOverlayPermission(context: Context): Boolean {
    return Settings.canDrawOverlays(context);
}

fun hasAccessibilityPermission(context: Context): Boolean {
    return isAccessibilitySettingsOn(context, UrlBlockerService::class.java)
}