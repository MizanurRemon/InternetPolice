package com.internetpolice.app.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.InfoItemCompose
import com.internetpolice.core.designsystem.components.ProfileImageCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeModalDrawerSheet(
    score: Int,
    isLoggedIn: Boolean,
    isProfileComplete: Boolean,
    userName: String,
    userImageUrl: String,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onMySubscriptionClick: () -> Unit,
    onMyProfileClick: (Boolean) -> Unit,
    onAccountSettingsClick: () -> Unit,
    onAccountCreateClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onProfileEditClick: () -> Unit
) {
    val openLogoutDialog = remember {
        mutableStateOf(false)
    }
    ShowPopup(
        openDialog = openLogoutDialog,
        titleResId = CommonR.string.log_out,
        descriptionResId = CommonR.string.log_out_details,
        dismissTextResId = CommonR.string.cancel,
        confirmTextResId = CommonR.string.log_out,
        important = false
    ) {
        onLogOutClick()
    }

    ModalDrawerSheet(
        modifier = if (drawerState.isOpen) Modifier.width(364.w()) else Modifier,
        drawerTonalElevation = 0.dp,
        drawerShape = RectangleShape,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .verticalScroll(rememberScrollState())
                .padding(top = 45.h(), end = 14.w()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.End)
                    .noRippleClickable {
                        scope.launch {
                            drawerState.close()
                        }
                    })

            Box(modifier = Modifier.scale(.9f)) {
                ProfileImageCompose(
                    enablePress = false,
                    score = score,
                    profileImageUrl = userImageUrl,
                    modifier = Modifier
                        .scale(.9f)
                        .noRippleClickable {
                            onMyProfileClick(!isLoggedIn)
                        },
                    isGuest = !isLoggedIn,
                    onEditClick = onProfileEditClick
                )
            }

            Text(
                text = if (isLoggedIn) userName else stringResource(id = CommonR.string.guest),
                style = heading1TextStyle
            )
            Spacer(modifier = Modifier.height(20.h()))
            Text(
                text = if (isLoggedIn) stringResource(id = CommonR.string.internet_police_agent) else stringResource(
                    id = CommonR.string.no_ranking
                ), style = bodyRegularTextStyle
            )

            Spacer(modifier = Modifier.height(44.h()))

            if (!isLoggedIn) {
                Box(modifier = Modifier.padding(start = 32.w(), end = 18.w())) {
                    AppActionButtonCompose(
                        stringId = CommonR.string.create_account
                    ) {
                        onAccountCreateClick()
                    }
                }
                Spacer(modifier = Modifier.height(40.h()))
            }

            if (isLoggedIn && isProfileComplete) {
                ProfileCardCompose(
                    iconResId = DesignSystemR.drawable.ic_profile,
                    titleResId = CommonR.string.my_profile
                ) {
                    onMyProfileClick(false)
                }
            }
            /*  if (isLoggedIn) ProfileCardCompose(
                  iconResId = DesignSystemR.drawable.ic_my_subscription,
                  titleResId = CommonR.string.my_subscription
              ) {
                  onMySubscriptionClick()
              }*/
            if (isLoggedIn) ProfileCardCompose(
                iconResId = DesignSystemR.drawable.ic_account_settings,
                titleResId = CommonR.string.account_setting
            ) {
                onAccountSettingsClick()
            }
            if (isLoggedIn) ProfileCardCompose(
                iconResId = DesignSystemR.drawable.ic_logout, titleResId = CommonR.string.log_out
            ) {
                openLogoutDialog.value = true
                scope.launch {
                    drawerState.close()
                }
            }
            if (!isLoggedIn) InfoCompose()
            Spacer(modifier = Modifier.weight(1f))
            if (isLoggedIn) Text(
                text = stringResource(id = CommonR.string.let_us_know_what_you_think),
                style = bodyBoldTextStyle.copy(color = Color(0xff3E6FCC)),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 24.h(), top = 24.h(), start = 50.h())
            )
        }
    }
}

@Composable
@DevicePreviews
private fun InfoCompose() {
    return Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center
    ) {
        InfoItemCompose(
            titleStringResId = CommonR.string.report_websites,
            iconResId = DesignSystemR.drawable.ic_checked
        )
        Spacer(modifier = Modifier.height(8.h()))
        InfoItemCompose(
            titleStringResId = CommonR.string.gain_experience_points,
            iconResId = DesignSystemR.drawable.ic_checked
        )
        Spacer(modifier = Modifier.height(8.h()))
        InfoItemCompose(
            titleStringResId = CommonR.string.personal_avatar,
            iconResId = DesignSystemR.drawable.ic_checked
        )
        Spacer(modifier = Modifier.height(8.h()))
        InfoItemCompose(
            titleStringResId = CommonR.string.access_premium_feature,
            iconResId = DesignSystemR.drawable.ic_checked
        )

    }
}


@Composable
private fun ProfileCardCompose(
    @DrawableRes iconResId: Int,
    @StringRes titleResId: Int, onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 32.w(), vertical = 6.h())
            .noRippleClickable { onClick() },
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(modifier = Modifier.padding(20.r()), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(18.w()))
            Text(stringResource(id = titleResId), style = bodyMediumTextStyle)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = DesignSystemR.drawable.ic_chevron_right),
                contentDescription = null,
                Modifier.size(24.r())
            )
        }
    }
}

@Composable
@Preview
fun PreviewCompose() {
//    ProfileCardCompose(
//        iconResId = DesignSystemR.drawable.ic_profile,
//        titleResId = CommonR.string.my_profile
//    ) {
//
//    }
}


@Composable
@Preview
fun PreviewProfileCardCompose() {
//    ProfileCardCompose(
//        iconResId = DesignSystemR.drawable.ic_profile,
//        titleResId = CommonR.string.my_profile
//    ) {
//
//    }
}
