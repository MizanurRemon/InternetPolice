package com.internetpolice.settings_presentation.screens.settings

import android.annotation.SuppressLint
import android.view.Gravity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.settings_presentation.helpers.ScreenSize
import java.util.*
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR


const val cardElevation = 1.2
const val carTopBottomPadding = 14

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    versionName: String,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit,
    onFAQClick: () -> Unit,
    onAboutUsClick: () -> Unit,
    onHelpContactClick: () -> Unit,
    onReportProblemClick: () -> Unit,
    onConditionClick: () -> Unit,
    onChangeLogClick: () -> Unit,
) {


    val buildVersion = remember {
        mutableStateOf(versionName)
    }


    var showPermissionDialog by remember {
        mutableStateOf(false)
    }

    var showTurnOffNotificationDialog by remember {
        mutableStateOf(false)
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.w(), top = 24.h(), end = 16.w())
        ) {

            AppToolbarCompose(title = stringResource(id = CommonR.string.home)) {
                onBack()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                LanguageChangeCompose(viewModel.state.language, onLanguageClick)
                NotificationsChangeCompose(viewModel.state.isNotificationOn) {
                    if (it) {
                        showPermissionDialog = true
                        showTurnOffNotificationDialog = false
                    } else {
                        showPermissionDialog = false
                        showTurnOffNotificationDialog = true
                    }
                }

                Divider(
                    modifier = Modifier
                        .padding(top = 10.r(), bottom = 10.r())
                        .wrapContentSize(), color = Color.LightGray
                )

                SettingsItemCard(
                    title = stringResource(id = CommonR.string.faq),
                    DesignSystemR.drawable.ic_faq,
                    cardElevation,
                    onFAQClick,
                    onAboutUsClick,
                    onHelpContactClick,
                    onReportProblemClick,
                    onConditionClick,
                    onChangeLogClick
                )

                SettingsItemCard(
                    title = stringResource(id = CommonR.string.contact_us),
                    DesignSystemR.drawable.ic_email_us,
                    cardElevation,
                    onFAQClick,
                    onAboutUsClick,
                    onHelpContactClick,
                    onReportProblemClick,
                    onConditionClick,
                    onChangeLogClick
                )

                SettingsItemCard(
                    title = stringResource(id = CommonR.string.report_a_problem),
                    DesignSystemR.drawable.ic_report_warning,
                    cardElevation,
                    onFAQClick,
                    onAboutUsClick,
                    onHelpContactClick,
                    onReportProblemClick,
                    onConditionClick,
                    onChangeLogClick,
                )

                Divider(
                    modifier = Modifier
                        .padding(top = 10.r(), bottom = 10.r())
                        .wrapContentSize(), color = Color.LightGray
                )

                SettingsItemCard(
                    title = stringResource(id = CommonR.string.about_us),
                    DesignSystemR.drawable.ic_about_us,
                    cardElevation,
                    onFAQClick,
                    onAboutUsClick,
                    onHelpContactClick,
                    onReportProblemClick,
                    onConditionClick,
                    onChangeLogClick
                )

                SettingsItemCard(
                    title = stringResource(id = CommonR.string.conditions),
                    DesignSystemR.drawable.ic_condition,
                    cardElevation,
                    onFAQClick,
                    onAboutUsClick,
                    onHelpContactClick,
                    onReportProblemClick,
                    onConditionClick,
                    onChangeLogClick
                )
                SettingsItemCard(
                    title = stringResource(id = CommonR.string.changelog),
                    DesignSystemR.drawable.ic_changelog,
                    cardElevation,
                    onFAQClick,
                    onAboutUsClick,
                    onHelpContactClick,
                    onReportProblemClick,
                    onConditionClick,
                    onChangeLogClick
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.h(), bottom = 20.h()),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(end = 5.w()),
                        text = stringResource(id = CommonR.string.buildversion),
                        style = bodyMediumTextStyle
                    )
                    Text(
                        text = buildVersion.value,
                        style = bodyMediumTextStyle.copy(color = Color.Gray)
                    )
                }
            }
        }
    }

    NotificationPermissionDialog(
        permissionDialogState = showPermissionDialog,
    ) {
        showPermissionDialog = false
        viewModel.updateNotificationStatus(it)
    }

    NotificationDeniedDialog(
        deniedDialogState = showTurnOffNotificationDialog,
    ) {
        showTurnOffNotificationDialog = false
        viewModel.updateNotificationStatus(it)
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun NotificationsChangeCompose(
    isNotificationEnabled: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
) {

    return Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults
            .elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 6.dp,
                    end = 6.dp,
                    top = carTopBottomPadding.dp,
                    bottom = carTopBottomPadding.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(DesignSystemR.drawable.ic_notifications),
                tint = ColorPrimaryDark,
                contentDescription = ""
            )

            Text(
                modifier = Modifier.padding(start = 18.dp),
                text = stringResource(id = CommonR.string.notifications),
                style = bodyMediumTextStyle
            )

            Spacer(modifier = Modifier.weight(1f))
            Switch(
                modifier = Modifier.scale(scale = 0.7f),
                checked = isNotificationEnabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ColorPrimaryDark,
                    checkedTrackColor = ColorTextPrimary,
                    uncheckedThumbColor = ColorPrimaryDark,
                    checkedBorderColor = Color.Transparent,
                    uncheckedBorderColor = Color.Transparent,
                    uncheckedTrackColor = Color(0xffDAE7FF),
                ),
                thumbContent = {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color = ColorPrimaryDark, shape = CircleShape)
                    )
                },
                onCheckedChange = { state ->
                    onCheckedChange?.invoke(state)
                })
            Spacer(modifier = Modifier.padding(end = 14.dp))
        }
    }
}

@Composable
private fun LanguageChangeCompose(
    language: String,
    onLanguageClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp, bottom = 6.dp)
            .clickable {
                onLanguageClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults
            .elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 6.dp,
                    end = 6.dp,
                    top = carTopBottomPadding.dp,
                    bottom = carTopBottomPadding.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(DesignSystemR.drawable.ic_language),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.padding(start = 18.dp),
                text = stringResource(id = CommonR.string.language),
                style = bodyMediumTextStyle
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.padding(end = 10.dp),
                text = language,
                style = bodyRegularTextStyle.copy(color = Color(0xff777A95))
            )
            Icon(
                painter = painterResource(id = DesignSystemR.drawable.ic_chevron_right),
                tint = Color.LightGray,
                contentDescription = "",
                modifier = Modifier
            )
        }
    }
}

@Composable
fun SettingsItemCard(
    title: String,
    iconImage: Int,
    cardElevation: Double,
    onFAQClick: () -> Unit,
    onAboutUsClick: () -> Unit,
    onHelpContactClick: () -> Unit,
    onReportProblemClick: () -> Unit,
    onConditionClick: () -> Unit,
    onChangeLogClick: () -> Unit,
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 6.dp)
            .clickable {

                when (title) {
                    context.getString(CommonR.string.about_us) -> {
                        onAboutUsClick()
                    }

                    context.getString(CommonR.string.changelog) -> {
                        onChangeLogClick()
                    }

                    context.getString(CommonR.string.contact_us) -> {
                        onHelpContactClick()
                    }

                    context.getString(CommonR.string.faq) -> {
                        onFAQClick()
                    }

                    context.getString(CommonR.string.report_a_problem) -> {
                        onReportProblemClick()
                    }

                    context.getString(CommonR.string.conditions) -> {
                        onConditionClick()
                    }
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults
            .elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 6.dp,
                    end = 6.dp,
                    top = carTopBottomPadding.dp,
                    bottom = carTopBottomPadding.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Image(
                painter = painterResource(id = iconImage),
                contentDescription = "",
                modifier = Modifier.size(50.dp)
            )
            Text(
                modifier = Modifier.padding(start = 18.dp),
                text = title,
                style = bodyMediumTextStyle
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = DesignSystemR.drawable.ic_chevron_right),
                tint = Color.LightGray,
                contentDescription = "",
                modifier = Modifier
            )

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NotificationPermissionDialog(
    permissionDialogState: Boolean,
    onDismissRequest: (isAllowed: Boolean) -> Unit
) {

    val bgColor = Color.White

    if (permissionDialogState)
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = {
                onDismissRequest(true)
            },
        ) {
            val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
            dialogWindowProvider.window.setGravity(Gravity.CENTER)
            Surface(
                color = bgColor,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(
                        start = (ScreenSize.width() / 5).dp,
                        end = (ScreenSize.width() / 5).dp
                    )
                    .fillMaxWidth()

            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = CommonR.string.notification_title),
                                style = TextStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = fontRoboto,
                                    fontSize = 18.sp,
                                    letterSpacing = .5.sp
                                )
                            )

                            Divider(color = Color.Transparent, modifier = Modifier.height(10.h()))

                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = CommonR.string.notification_msg),
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier.padding(top = 10.dp),
                        thickness = 1.dp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.h())
                    ) {
                        TextButton(
                            onClick = { onDismissRequest(false) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = stringResource(id = CommonR.string.don_allow),
                                style = TextStyle(color = ColorPrimaryDark)
                            )
                        }

                        Divider(
                            color = Color.LightGray,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.w())
                        )


                        TextButton(
                            onClick = {
                                onDismissRequest(true)
                            }, modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = stringResource(id = CommonR.string.ok),
                                style = TextStyle(color = ColorPrimaryDark)
                            )
                        }
                    }
                }


            }
        }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NotificationDeniedDialog(
    deniedDialogState: Boolean,
    onDismissRequest: (isEnable: Boolean) -> Unit,
) {

    val bgColor = Color.White

    if (deniedDialogState) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = {
                onDismissRequest(true)
            },
        ) {

            val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
            dialogWindowProvider.window.setGravity(Gravity.CENTER)

            Surface(
                color = bgColor,
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = CommonR.string.turn_off_notifications),
                                style = subHeading1TextStyle
                            )

                            Divider(color = Color.Transparent, modifier = Modifier.height(30.h()))

                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 15.dp, end = 15.dp),
                                text = stringResource(id = CommonR.string.turn_off_notifications_confirm_msg).parseBold(),
                                style = bodyRegularTextStyle.copy(color = ColorTextSecondary)
                            )
                        }
                    }

                    Image(
                        painter = painterResource(id = DesignSystemR.drawable.ic_police_cross),
                        contentDescription = "",
                        modifier = Modifier.size((ScreenSize.height() / 3).dp),
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(ColorPrimaryDark),
                            onClick = {
                                onDismissRequest(true)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.h())
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = CommonR.string.cancel),
                                style = TextStyle(
                                    color = Color.White,
                                    fontFamily = fontRoboto,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    letterSpacing = .5.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(12.w()))
                        Button(
                            shape = RoundedCornerShape(30.dp),
                            border = BorderStroke(1.dp, Color.Red),
                            colors = ButtonDefaults.buttonColors(Color.White),
                            onClick = {
                                onDismissRequest(false)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.h())
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = CommonR.string.turn_off),
                                style = TextStyle(
                                    color = Color.Red,
                                    fontFamily = fontRoboto,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    letterSpacing = .5.sp
                                )
                            )
                        }
                    }

                }


            }
        }
    }
}

@Composable
@DevicePreviews
fun PreviewSettingsScreen() {
    SettingsScreen(
        versionName = "1.1.2",
        onBack = {},
        onLanguageClick = {},
        onFAQClick = {},
        onAboutUsClick = {},
        onHelpContactClick = {},
        onReportProblemClick = {},
        onConditionClick = {},
        onChangeLogClick = {})
}
