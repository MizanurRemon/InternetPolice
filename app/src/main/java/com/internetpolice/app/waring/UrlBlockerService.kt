package com.internetpolice.app.waring

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.net.Uri
import android.provider.Browser
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.mutableStateOf
import androidx.core.accessibilityservice.AccessibilityServiceInfoCompat
import androidx.core.content.ContextCompat
import com.internetpolice.app.R
import com.internetpolice.core.common.util.APP_SETTINGS
import com.internetpolice.core.common.util.LANGUAGE_KEY
import com.internetpolice.core.common.util.btoa
import com.internetpolice.core.common.util.getDomainName
import com.internetpolice.core.common.util.getHostName
import com.internetpolice.core.common.util.getUrlWithProtocol
import com.internetpolice.core.common.util.updateResourcesLanguage
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.designsystem.languageList
import com.internetpolice.core.network.dto.DomainDetailsDto
import com.internetpolice.core.network.dto.ScrapDataDto
import com.internetpolice.core.network.model.ScrapDataRequest
import com.internetpolice.core.network.service_retrofit.RetrofitInstance
import com.internetpolice.core.network.service_retrofit.RetrofitInstanceForScrapping
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicReference
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.common.util.REDIRECT_URL

class UrlBlockerService : AccessibilityService() {
    private val previousUrlDetections = HashMap<String, Long>()
    private var windowManager: WindowManager? = null
    private var vBrowserBlockerOverlay: View? = null
    private var params: WindowManager.LayoutParams? = null
    private var capturedUrl: String? = null
    private var browserConfig: SupportedBrowserConfig? = null
    private val redirectUrl = REDIRECT_URL

    private var isUrlCheckingRunning: Boolean = false
    private var isScrapCheckingRunning: Boolean = false
    private var isLoggedIn = AtomicReference(false)
    private var isNotificationOn = AtomicReference(false)

    private lateinit var updatedResources: Resources
    private lateinit var sharedPreferences: SharedPreferences
    private var largeImageInt: Int = DesignSystemR.drawable.ic_logo_vertical_dutch
    private var timerJob: Job? = null

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, updatedKey ->
        if (updatedKey == LANGUAGE_KEY) {
            val tag = sharedPreferences.getString(
                LANGUAGE_KEY, AppCompatDelegate.getApplicationLocales().toLanguageTags()
            ).toString()

            updatedResources = updateResourcesLanguage(
                tag, applicationContext
            )

            largeImageInt = if (tag == languageList[1].tag) {
                DesignSystemR.drawable.ic_logo_vertical_dutch
            } else {
                DesignSystemR.drawable.ic_logo_vertical
            }

        }
    }


    companion object {
        var trustedUrls = mutableListOf<DomainDetailsDto>()
    }


    override fun onServiceConnected() {
        val info = serviceInfo
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        info.packageNames = getBrowserPackageNames()
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
        //throttling of accessibility event notification
        info.notificationTimeout = 300
        //support ids interception
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS or
                AccessibilityServiceInfoCompat.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE or
                AccessibilityServiceInfoCompat.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY or
                AccessibilityServiceInfoCompat.FLAG_REQUEST_FILTER_KEY_EVENTS or
                AccessibilityServiceInfoCompat.FLAG_REPORT_VIEW_IDS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS or
                AccessibilityServiceInfo.FLAG_ENABLE_ACCESSIBILITY_VOLUME or
                AccessibilityServiceInfo.FLAG_REQUEST_ACCESSIBILITY_BUTTON or
                AccessibilityServiceInfo.FLAG_REQUEST_SHORTCUT_WARNING_DIALOG_SPOKEN_FEEDBACK or
                AccessibilityServiceInfo.FLAG_INPUT_METHOD_EDITOR
        this.serviceInfo = info

        //initiate overlay related tasks
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        vBrowserBlockerOverlay =
            LayoutInflater.from(this).inflate(R.layout.layout_browser_blocker_overlay, null)

        CoroutineScope(Dispatchers.IO).launch {
            async { updateLoginValue() }
            async { updateNotificationValue() }
        }


        sharedPreferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)
        updatedResources = updateResourcesLanguage(
            AppCompatDelegate.getApplicationLocales().toLanguageTags(),
            applicationContext
        )
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

    }

    private suspend fun updateLoginValue() {
        PreferenceDataStoreHelper(applicationContext).getPreference(
            PreferenceDataStoreConstants.IS_LOGGED_IN, false
        ).collect {
            isLoggedIn.set(it)

        }
    }


    private suspend fun updateNotificationValue() {
        PreferenceDataStoreHelper(applicationContext).getPreference(
            PreferenceDataStoreConstants.IS_Notification_Enable, false
        ).collect {
            isNotificationOn.set(it)
        }
    }


    private fun captureUrl(info: AccessibilityNodeInfo, config: SupportedBrowserConfig?): String? {
        val nodes = info.findAccessibilityNodeInfosByViewId(
            config!!.addressBarId
        )
        if (nodes == null || nodes.size == 0) {
            return null
        }
        val addressBarNodeInfo = nodes[0]

        var url: String? = null
        if (addressBarNodeInfo.text != null) {
            url = addressBarNodeInfo.text.toString()
        }
        return url
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!isLoggedIn.get()) return

        val parentNodeInfo = event.source ?: return
        val packageName = event.packageName.toString()
        val nodeInfo = parentNodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
        val action = nodeInfo?.actionList?.find { it.id == AccessibilityNodeInfo.ACTION_CLICK }
        val isActionDoneClickByKeyboard = (action == null)
        browserConfig = null
        for (supportedConfig in supportedBrowsers) {
            if (supportedConfig.packageName == packageName) {
                browserConfig = supportedConfig
            }
        }
        //this is not supported browser, so exit
        if (browserConfig == null) return

        capturedUrl = captureUrl(parentNodeInfo, browserConfig)

        //we can't find a url. Browser either was updated or opened page without url text field
        if (capturedUrl == null) {
            return
        }
        val eventTime = event.eventTime
        val detectionId = "$packageName, and url $capturedUrl"
        val lastRecordedTime =
            if (previousUrlDetections.containsKey(detectionId)) previousUrlDetections[detectionId]!! else 0

        //some kind of redirect throttling
        if (eventTime - lastRecordedTime > 2000 && isActionDoneClickByKeyboard) {
            previousUrlDetections[detectionId] = eventTime
            capturedUrl?.let { capturedUrl ->
                browserConfig?.let {

                    if (trustedUrls.any {
                            capturedUrl.contains(
                                it.domain ?: ""
                            )
                        }) {

                        timerJob = CoroutineScope(Dispatchers.IO).launch {
                            repeat(10) {
                                delay(1000)
                            }
                            trustedUrls.clear()
                        }

                    }

                    if (!isUrlCheckingRunning && isCheckableUrl(capturedUrl) && !trustedUrls.any {
                            capturedUrl.contains(
                                it.domain ?: ""
                            )
                        }) {
                        val url = capturedUrl
                        isUrlCheckingRunning = true
                        showBrowserBlockerOverlay()



                        CoroutineScope(Dispatchers.Main).launch {


                            runBlocking {


                                var domainDetailsDto: DomainDetailsDto? = analyzeCapturedUrl(url)

                                if (domainDetailsDto != null && domainDetailsDto.trustScore < 70) {
                                    domainDetailsDto = domainDetailsDto.copy(
                                        capturedUrl = url, browserPackageName = it.packageName
                                    )
                                    if (isNotificationOn.get()) {
                                        showWarningNotification(
                                            domainDetailsDto = domainDetailsDto,
                                            context = applicationContext,
                                            body = updatedResources.getString(CommonR.string.website_blocked_by_internet_police),
                                            title = updatedResources.getString(CommonR.string.please_click_for_showing_more_details),
                                            smallIconDrawable = android.R.drawable.stat_sys_warning,
                                            largeImageDrawable = getBitmapFromImage(
                                                applicationContext, largeImageInt
                                            )
                                        )
                                    }

                                    performRedirect(it.packageName)
                                }

                                if (domainDetailsDto != null && domainDetailsDto.isECommerceScrapEnabled == true) {

                                    val scrapDataRequest = ScrapDataRequest(
                                        domainName = getDomainName(capturedUrl),
                                        url = getUrlWithProtocol(capturedUrl)
                                    )

                                    val scrapDataDto: ScrapDataDto? =
                                        scrappingAnalyzeFromCapturesUrl(scrapDataRequest)

                                    if (scrapDataDto != null && scrapDataDto.exist) {
                                        isScrapCheckingRunning = true
                                        if (isNotificationOn.get()) scrapNotificationUtils(
                                            context = applicationContext,
                                            body = "${scrapDataDto.sellerName} ${
                                                updatedResources.getString(
                                                    CommonR.string.relatively_new_seller
                                                )
                                            } ${getHostName(getDomainName(capturedUrl))}. ${
                                                updatedResources.getString(
                                                    CommonR.string.scammers_often_use
                                                )
                                            }",
                                            title = updatedResources.getString(CommonR.string.please_be_aware),
                                            smallIconDrawable = android.R.drawable.stat_sys_warning,
                                            largeImageDrawable = getBitmapFromImage(
                                                applicationContext,
                                                DesignSystemR.drawable.ic_warning_logo
                                            )
                                        )
                                    }

                                }

                                hideBrowserBlockerOverlay()
                                isUrlCheckingRunning = false
                            }

                        }


                    }
                }

            }
        }

    }


    private fun findViewByTestTag(
        node: AccessibilityNodeInfo?, testTag: String
    ): AccessibilityNodeInfo? {
        if (node == null) return null

        for (i in 0 until node.childCount) {
            val childNode = node.getChild(i)
            if (childNode.viewIdResourceName == testTag) {
                return childNode
            }
            val foundNode = findViewByTestTag(childNode, testTag)
            if (foundNode != null) {
                return foundNode
            }
        }

        return null
    }


    private suspend fun analyzeCapturedUrl(
        capturedUrl: String,
    ): DomainDetailsDto? {
        val encodeUrl = btoa(getDomainName(capturedUrl.trim()))
        return try {
            RetrofitInstance.getApiInstance().domainDetails(encodeUrl)
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun scrappingAnalyzeFromCapturesUrl(scrapDataRequest: ScrapDataRequest): ScrapDataDto? {

        return try {
            RetrofitInstanceForScrapping.getApiInstance().getScrapData(scrapDataRequest)
        } catch (e: Exception) {
            null
        }
    }


    private fun isCheckableUrl(
        capturedUrl: String,
    ): Boolean {
        //we should not show overlay when redirect and capture url is same
        //and when a url is not a valid url
        return (redirectUrl.replace("https://", "")
                != capturedUrl.replace("https://", "")
                && !capturedUrl.contains(" ") &&
                !isASearchEngineQuery(capturedUrl))
    }

    private fun showBrowserBlockerOverlay() {
        try {
            windowManager?.addView(vBrowserBlockerOverlay, params)
        } catch (exception: Exception) {
            Log.e(UrlBlockerService::class.java.simpleName, exception.message ?: "")
        }
    }

    private fun hideBrowserBlockerOverlay() {
        try {
            if (vBrowserBlockerOverlay?.parent != null) windowManager?.removeView(
                vBrowserBlockerOverlay
            )
        } catch (exception: Exception) {
            Log.e(UrlBlockerService::class.java.simpleName, exception.message!!)
        }
    }

    private fun performRedirect(browserPackage: String) {

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
            intent.setPackage(browserPackage)
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, browserPackage)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        or Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // the expected browser is not installed
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
            startActivity(i)
        }
    }

    private fun performRedirectWarning(browserPackage: String) {

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
            intent.setPackage(browserPackage)
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, browserPackage)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        or Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // the expected browser is not installed
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
            startActivity(i)
        }
    }

    override fun onInterrupt() {}

    private fun getBitmapFromImage(context: Context, drawable: Int): Bitmap {
        val db = ContextCompat.getDrawable(context, drawable)
        val bit = Bitmap.createBitmap(
            db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bit)
        db.setBounds(0, 0, canvas.width, canvas.height)
        db.draw(canvas)
        return bit
    }


}
