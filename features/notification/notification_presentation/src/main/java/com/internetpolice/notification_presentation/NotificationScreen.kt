package com.internetpolice.notification_presentation

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.designsystem.OnLifecycleEvent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.designScreenWidth
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.languageList
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.ColorTextSecondary
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.fontRoboto
import com.internetpolice.core.designsystem.theme.heading1TextStyle
import com.internetpolice.core.designsystem.theme.parseBold
import com.internetpolice.core.designsystem.theme.subHeading1TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun NotificationScreen(
    navController: NavController,
    isHome: Boolean = false,
    viewModel: NotificationViewModel = hiltViewModel(),
) {

    return Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(start = 18.w(), end = 18.w(), top = 40.h())
    ) {


        val context = LocalContext.current
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val hasNotificationPolicyAccessPermission = remember {
            mutableStateOf(notificationManager.isNotificationPolicyAccessGranted)
        }
        val hasNotificationEnabled = remember {
            mutableStateOf(notificationManager.areNotificationsEnabled())
        }
        val isTriedNotificationPolicyAccessClicked = remember {
            mutableStateOf(false)
        }
        val isTriedNotificationEnabled = remember {
            mutableStateOf(false)
        }

        val isTriedBothNotificationClicked = remember {
            mutableStateOf(false)
        }

        val isInteracting = remember {
            mutableStateOf(false)
        }


        OnLifecycleEvent { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {

                    hasNotificationPolicyAccessPermission.value =
                        notificationManager.isNotificationPolicyAccessGranted
                    hasNotificationEnabled.value = notificationManager.areNotificationsEnabled()

                    if (isTriedNotificationEnabled.value) {
                        isTriedNotificationPolicyAccessClicked.value = true
                        isTriedNotificationEnabled.value = false
                        isTriedBothNotificationClicked.value = true
                        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                        startActivity(context, intent, null)
                    }

                }

                else -> {
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.padding(start = 40.w(), end = 40.w())) {
                Image(
                    painter = if (viewModel.state.tag == languageList[1].tag) {
                        painterResource(id = DesignSystemR.drawable.ic_logo_horizontal_dutch)
                    } else {
                        painterResource(id = DesignSystemR.drawable.ic_logo_horizontal)
                    },
                    contentDescription = null,
                    modifier = Modifier
                       // .width(320.w())
                        .background(Color.Transparent)
                )
            }

            Spacer(modifier = Modifier.height(50.h()))
            Image(
                painter = painterResource(
                    id = if (hasNotificationPolicyAccessPermission.value && hasNotificationEnabled.value) DesignSystemR.drawable.ic_cop_notification_enable
                    else DesignSystemR.drawable.ic_cop_notification_disable
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .width((designScreenWidth / 1.43).dp)
                    .background(Color.Transparent),
            )

            Spacer(modifier = Modifier.height(20.h()))

            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(
                    start = 40.w(), end = 40.w()
                )
            ) {
                Text(
                    text = stringResource(
                        id = if (hasNotificationPolicyAccessPermission.value && hasNotificationEnabled.value) CommonR.string.you_are_protected
                        else CommonR.string.notification_disabled
                    ), style = heading1TextStyle.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.W700,
                        lineHeight = 33.ssp(),
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.width(10.w()))
                Image(
                    painter = painterResource(
                        id = if (hasNotificationPolicyAccessPermission.value && hasNotificationEnabled.value) DesignSystemR.drawable.ic_avatar_component_selected_primary_bg
                        else DesignSystemR.drawable.ic_notification_off
                    ), contentDescription = null, modifier = Modifier.size(30.w())
                )
            }

            Text(
                text = stringResource(
                    id = if (hasNotificationPolicyAccessPermission.value && hasNotificationEnabled.value) CommonR.string.notification_enable_info
                    else if (isTriedNotificationPolicyAccessClicked.value) CommonR.string.notification_disabled_post_info
                    else CommonR.string.notification_disabled_pre_info
                ).parseBold(),
                style = bodyRegularTextStyle.copy(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.W400,
                    color = ColorTextSecondary,
                    fontSize = 16.ssp(),
                    lineHeight = 25.ssp(),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(
                    top = 20.h(), start = 40.w(), end = 40.w(), bottom = 20.h()
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            AppActionButtonCompose(
                stringId = if ((hasNotificationEnabled.value && hasNotificationPolicyAccessPermission.value) || (isTriedNotificationPolicyAccessClicked.value || isTriedNotificationEnabled.value)) CommonR.string.continues else CommonR.string.turn_on_notification,
                modifier = Modifier.padding(start = 18.w(), end = 18.w(), bottom = 55.h())
            ) {
                isInteracting.value = true
                if (isTriedBothNotificationClicked.value || (hasNotificationEnabled.value && hasNotificationPolicyAccessPermission.value)) {
                    viewModel.updateNotificationStatus(
                        hasNotificationEnabled.value && hasNotificationPolicyAccessPermission.value
                    )
                    if (isHome) navController.navigate(Route.HOME) {
                        popUpTo(navController.graph.id)
                    }
                    else navController.navigate(Route.ONBOARDING)
                } else if (!hasNotificationEnabled.value) {
                    isTriedNotificationEnabled.value = true
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    intent.putExtra(
                        Settings.EXTRA_APP_PACKAGE, context.packageName
                    )
                    startActivity(context, intent, null)
                } else if (!hasNotificationPolicyAccessPermission.value) {
                    isTriedNotificationPolicyAccessClicked.value = true
                    isTriedBothNotificationClicked.value = true
                    val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    startActivity(context, intent, null)
                }
            }
        }


    }


}


@Composable
@DevicePreviews
fun PreviewNotificationScreen() {
//      NotificationScreen(isHome = false, navController = rememberNavController())
}