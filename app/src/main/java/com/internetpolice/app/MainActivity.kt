package com.internetpolice.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.internetpolice.app.connectivity.NetworkCallbackImpl
import com.internetpolice.app.connectivity.NetworkStatusScreen
import com.internetpolice.app.navigations.IPApp
import com.internetpolice.app.waring.NOTIFICATION_CLICKED_DOMAIN_DETAILS_DTO
import com.internetpolice.core.designsystem.deviceHeight
import com.internetpolice.core.designsystem.deviceWidth
import com.internetpolice.core.designsystem.theme.InternetPoliceTheme
import com.internetpolice.core.network.dto.DomainDetailsDto
import com.internetpolice.report_domain.model.DomainModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var newIntent: Intent? = null

    private var appUpdateManager: AppUpdateManager? = null
    private var installStateUpdatedListener: InstallStateUpdatedListener? = null

    private val networkCallback = NetworkCallbackImpl()

    private var activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                unregisterInstallStateUpdListener()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(resources.configuration)
        deviceWidth = with(resources.displayMetrics) {
            (widthPixels / density).toInt()
        }
        deviceHeight = with(resources.displayMetrics) {
            (heightPixels / density).toInt()
        }

        val domainDetailsDto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(
                NOTIFICATION_CLICKED_DOMAIN_DETAILS_DTO, DomainDetailsDto::class.java
            )
        } else {
            intent.extras?.getParcelable(
                NOTIFICATION_CLICKED_DOMAIN_DETAILS_DTO
            )
        }
        var domainModel: DomainModel? = null
        if (intent?.action == Intent.ACTION_SEND) {
            if ("text/plain" == intent.type) {
                intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    domainModel = DomainModel(
                        id = -1,
                        negativeResultCount = 0,
                        domain = it,
                        trustScore = 0.0,
                        voteCount = 0
                    )
                }
            }
        }

        setContent {
            InternetPoliceTheme {
                IPApp(domainDetailsDto = domainDetailsDto, domainModel = domainModel)
                NetworkStatusScreen()
            }
        }

        checkForAppUpdate()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        newIntent = intent
    }

    override fun onResume() {
        super.onResume()
        newIntent?.let {
            var domainModel: DomainModel? = null
            if (it?.action == Intent.ACTION_SEND) {
                if ("text/plain" == it.type) {
                    it.getStringExtra(Intent.EXTRA_TEXT)?.let { url ->
                        domainModel = DomainModel(
                            id = -1,
                            negativeResultCount = 0,
                            domain = url,
                            trustScore = 0.0,
                            voteCount = 0
                        )
                    }
                }
            }
            setContent {
                IPApp(domainDetailsDto = null, domainModel = domainModel)
            }
            newIntent = null
        }
    }

    override fun onStart() {
        super.onStart()
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onStop() {
        super.onStop()
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun checkForAppUpdate() {

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager!!.appUpdateInfo

        installStateUpdatedListener =
            InstallStateUpdatedListener { installState ->
                if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                    appUpdateManager!!.completeUpdate()
                } else if (installState.installStatus() == InstallStatus.DOWNLOADING) {

                }
            }

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    appUpdateManager!!.registerListener(installStateUpdatedListener!!)
                    startAppUpdateFlexible(appUpdateInfo)
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    startAppUpdateFlexible(appUpdateInfo)
                }
            }
        }
    }

    private fun startAppUpdateImmediate(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                activityResultLauncher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun startAppUpdateFlexible(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                activityResultLauncher,
                AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
            unregisterInstallStateUpdListener()
        }
    }

    private fun unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null) appUpdateManager!!.unregisterListener(
            installStateUpdatedListener!!
        )
    }

    private fun adjustFontScale(configuration: Configuration) {
        configuration.let {
            it.fontScale = .9.toFloat()
            val metrics: DisplayMetrics = resources.displayMetrics
            val wm: WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density

            //baseContext.resources.updateConfiguration(configuration, metrics)
            baseContext.applicationContext.createConfigurationContext(it)
            baseContext.resources.displayMetrics.setTo(metrics)

        }
    }
}
