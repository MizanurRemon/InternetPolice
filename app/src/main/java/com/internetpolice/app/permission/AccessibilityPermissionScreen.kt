package com.internetpolice.app.permission

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.internetpolice.app.navigations.openAccessibilitySettings
import com.internetpolice.core.designsystem.OnLifecycleEvent
import com.internetpolice.core.designsystem.R.drawable
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.ColorPrimaryDark
import com.internetpolice.core.designsystem.theme.ConsentPopUp
import com.internetpolice.core.designsystem.theme.bodyRegularSpanStyle
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.parseBold
import com.internetpolice.core.designsystem.theme.subHeading1TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.common.R as CommonR

@Composable
fun AccessibilityPermissionScreen(
    navController: NavController,
    onClose: () -> Unit,
    viewModel: AccessibilityViewModel = hiltViewModel()
) {


    val context = LocalContext.current

    val openConsentDialog = remember {
        mutableStateOf(false)
    }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
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
                top = 20.h(),
                bottom = 40.h()
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ConstraintLayout {

            val (appToolbarCompose, crossIcon) = createRefs()

            AppToolbarCompose(
                title = "",
                modifier =
                Modifier
                    .background(Color.Transparent)
                    .constrainAs(appToolbarCompose) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                onClose()
            }

            Image(
                painter = painterResource(id = com.internetpolice.core.designsystem.R.drawable.ic_blue_cross),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .constrainAs(crossIcon) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        onClose()
                    },
            )
        }


        Image(
            painter = painterResource(id = drawable.ic_accessibility_master_permission),
            contentDescription = null,
            modifier = Modifier
                .size(100.h())
                .background(color = Color.Transparent)
        )
        Spacer(modifier = Modifier.height(15.h()))

        val headingString = buildAnnotatedString {
            append(stringResource(id = CommonR.string.grant) + " ")
            withStyle(style = SpanStyle(ColorPrimaryDark)) {
                append(stringResource(CommonR.string.accessibility) + " ")
            }
            append(stringResource(id = CommonR.string.permission))
        }
        Text(
            text = headingString,
            style = subHeading1TextStyle
        )

        Spacer(modifier = Modifier.height(30.h()))
        Text(
            text =
            stringResource(id = CommonR.string.missing_permission_accessibility_details),
            style = bodyRegularTextStyle,
            modifier = Modifier.padding(vertical = 15.h())
        )
        Text(buildAnnotatedString {
            withStyle(
                style = bodyRegularSpanStyle.copy(fontWeight = FontWeight.Bold)
            ) {
                append("1. ")
            }
            withStyle(
                style = bodyRegularSpanStyle
            ) {
                append(stringResource(id = CommonR.string.accessibility_permission_step_1).parseBold())
            }
            append("\n")
            append("\n")
            withStyle(
                style = bodyRegularSpanStyle.copy(fontWeight = FontWeight.Bold)
            ) {
                append("2. ")
            }
            withStyle(
                style = bodyRegularSpanStyle
            ) {
                append(stringResource(id = CommonR.string.accessibility_permission_step_2).parseBold())
            }
            append("\n")
            append("\n")
            withStyle(
                style = bodyRegularSpanStyle.copy(fontWeight = FontWeight.Bold)
            ) {
                append("3. ")
            }
            withStyle(
                style = bodyRegularSpanStyle
            ) {
                append(stringResource(id = CommonR.string.accessibility_permission_step_3).parseBold())
            }

        })

        Spacer(modifier = Modifier.weight(.6f))
        AppActionButtonCompose(
            stringId = CommonR.string.go_to_settings,
        ) {
            viewModel.checkConsent()

            if (viewModel.isUserConsent.value) {
                openConsentDialog.value = viewModel.isUserConsent.value
            } else {
                openAccessibilitySettings(context = context)
            }
        }

        if (viewModel.isUserConsent.value) {
            ConsentPopUp(
                openDialog = openConsentDialog,
                cancelClick = {
                    openConsentDialog.value = false
                },
                agreeClick = {
                    viewModel.updateConsent()
                    openConsentDialog.value = false
                })
        }

    }
}


@DevicePreviews
@Composable
fun PreviewMasterPermissionAccessibilityScreen() {
    return AccessibilityPermissionScreen(rememberNavController(), onClose = {})
}