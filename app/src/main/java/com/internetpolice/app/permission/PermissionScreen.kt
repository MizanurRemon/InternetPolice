package com.internetpolice.app.permission

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.internetpolice.app.waring.hasAccessibilityPermission
import com.internetpolice.app.waring.hasOverlayPermission
import com.internetpolice.core.common.R
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.designsystem.OnLifecycleEvent
import com.internetpolice.core.designsystem.R.drawable
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.bodyMediumTextStyle
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.bodyXSRegularTextStyle
import com.internetpolice.core.designsystem.theme.noRippleClickable
import com.internetpolice.core.designsystem.theme.subHeading1TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PermissionScreen(
    navController: NavController,
    onDoneClick: () -> Unit
) {
    val context = LocalContext.current

    var count by remember { mutableStateOf(0) }
    var buttonEnabled by remember {
        mutableStateOf(false)
    }

    var accessibilityPermission by remember {
        mutableStateOf(false)
    }

    var overlayPermission by remember {
        mutableStateOf(false)
    }


    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                count = 0
                if (!hasAccessibilityPermission(context)) {
                    count++
                } else {
                    accessibilityPermission = false
                }

                if (!hasOverlayPermission(context)) {
                    count++
                } else {
                    overlayPermission = false
                }
            }

            else -> {
            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = AppBrush)
            .padding(
                start = 40.w(),
                end = 40.w(),
                top = 75.h(),
                bottom = 40.h()
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (count > 0) {
            Image(
                painter = painterResource(id = drawable.ic_permission_main),
                contentDescription = null
            )
        } else {
            Image(
                painter = painterResource(id = drawable.ic_all_permission_granted),
                contentDescription = "",
                modifier = Modifier.width(190.dp)
            )
        }
        Spacer(modifier = Modifier.height(15.h()))
        Row(verticalAlignment = Alignment.CenterVertically) {

            if (count > 0) {
                Text(
                    text = pluralStringResource(
                        id = CommonR.plurals.missing_permission_plural,
                        count = count,
                        formatArgs = arrayOf(count)
                    ),
                    style = subHeading1TextStyle
                )
                Spacer(modifier = Modifier.width(10.w()))

                Image(
                    painter = painterResource(
                        id = drawable.ic_red_warning
                    ), contentDescription = null
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(
                            id = CommonR.string.all_permission_granted
                        ),
                        style = subHeading1TextStyle
                    )

                    Spacer(modifier = Modifier.width(5.w()))

                    Image(
                        painter = painterResource(id = DesignSystemR.drawable.ic_avatar_component_selected),
                        contentDescription = "",
                        modifier = Modifier.size(28.w())
                    )
                }
            }
        }
        Text(
            text =
            stringResource(
                id = if (count > 0) {
                    CommonR.string.missing_permission_info
                } else {
                    CommonR.string.all_permission_granted_text
                }
            ),
            style = bodyRegularTextStyle,
            modifier = Modifier.padding(vertical = 15.h())
        )
        if (!hasAccessibilityPermission(context = context)) {
            MasterPermissionItemCompose(
                iconRes = drawable.ic_accessibility_master_permission,
                titleRes = CommonR.string.missing_permission_accessibility_title,
                descriptionRes = CommonR.string.missing_permission_accessibility_details
            ) {
                // navController.navigate(Route.PERMISSION_ACCESSIBILITY)
                accessibilityPermission = true

            }
        }
        if (!hasOverlayPermission(context = context)) {
            MasterPermissionItemCompose(
                iconRes = drawable.ic_elipsize_overlay_icon,
                titleRes = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) (CommonR.string.appear_on_top) else (CommonR.string.missing_permission_overlay_title),
                descriptionRes = CommonR.string.missing_permission_overlay_details
            ) {
                //  navController.navigate(Route.PERMISSION_OVERLAY)
                overlayPermission = true
            }
        }
        Spacer(modifier = Modifier.weight(.6f))

        buttonEnabled = count <= 0

        AppActionButtonCompose(
            stringId = CommonR.string.done,
            modifier = Modifier.padding(
                // bottom = 30.h(),
            ),
            enable = buttonEnabled
        ) {
            onDoneClick()
        }

        if (accessibilityPermission) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(30.dp)
                        .clip(shape = RoundedCornerShape(40.dp))
                ) {
                    AccessibilityPermissionScreen(navController = navController, onClose = {
                        accessibilityPermission = false
                    })
                }
            }

        }

        if (overlayPermission) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(30.dp)
                        .clip(shape = RoundedCornerShape(40.dp))
                ) {
                    OverlayPermissionScreen(navController = navController, onClose = {
                        overlayPermission = false
                    })
                }
            }
        }

    }
}

@Composable
fun MasterPermissionItemCompose(
    @DrawableRes iconRes: Int,
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .noRippleClickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults
            .elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation =
            1.r()
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 14.w(),
                    end = 14.w(),
                    top = 15.h(),
                    bottom = 12.h()
                ),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "",
                modifier = Modifier.size(24.r())
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.w())
            ) {
                Text(
                    text = stringResource(id = titleRes),
                    style = bodyMediumTextStyle
                )
                Spacer(modifier = Modifier.height(8.h()))
                Text(
                    text = stringResource(id = descriptionRes),
                    style = bodyXSRegularTextStyle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.heightIn(min = 46.h())
                )
            }
            Icon(
                painter = painterResource(id = drawable.ic_chevron_right),
                tint = Color(0xff718096),
                contentDescription = "",
                modifier = Modifier.size(24.r())
            )
        }
    }
}


@DevicePreviews
@Composable
fun PreviewPermissionMainScreen() {
    return PermissionScreen(
        rememberNavController(), onDoneClick = {}
    )
}