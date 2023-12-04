package com.internetpolice.settings_presentation.screens

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.common.util.openExternalLink

import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ConditionScreen(
    onBack: () -> Unit,
    //navController: NavHostController
) {


    var privacyPolicyDialogState by remember {
        mutableStateOf(false)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
    ) {
        Column {
            AppToolbarCompose(
                modifier = Modifier
                    .padding(start = 16.w(), top = 24.h(), end = 16.w()),
                title = stringResource(id = CommonR.string.back)
            ) {
                onBack()
            }


            Text(
                text = stringResource(id = CommonR.string.conditions),
                style = heading1TextStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.h(), bottom = 25.h()),
            )


            ConditionItemCompose(CommonR.string.conditions, "https://internetpolice.com/general-terms-conditions/") {

            }
            ConditionItemCompose(CommonR.string.privacy_policy, "https://internetpolice.com/privacy-policy/") {

            }

        }

    }

    PrivacyPolicyDialog(privacyPolicyDialogState = privacyPolicyDialogState) {
        privacyPolicyDialogState = !it
    }

}

@Composable
private fun ConditionItemCompose(@StringRes titleStringRes: Int, url: String, onClick: () -> Unit) {
    val context = LocalContext.current
    Card(colors = CardDefaults
        .elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .padding(vertical = 6.dp)
            .clickable {
                openExternalLink(url, context)
            }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.r()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Text(
                modifier = Modifier.padding(start = 30.w()),
                text = stringResource(id = titleStringRes),
                style = bodyMediumTextStyle
                    .copy(color = Color(0xff3E6FCC))
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = DesignSystemR.drawable.arrow_up_right_angle),
                tint = ColorPrimaryDark,
                modifier = Modifier
                    .padding(end = 14.w())
                    .size(24.r()),
                contentDescription = ""
            )
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyDialog(
    privacyPolicyDialogState: Boolean,
    onDismissRequest: (dialogState: Boolean) -> Unit
) {

    LocalContext.current

    val bgColor = Color.White
    if (privacyPolicyDialogState) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = {
                onDismissRequest(privacyPolicyDialogState)
            },
        ) {


            Surface(
                color = bgColor,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
            ) {
                Scaffold(
                    containerColor = Color.Transparent,
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        Button(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                            colors = ButtonDefaults.buttonColors(ColorPrimaryDark),
                            shape = RoundedCornerShape(20.dp),
                            onClick = { onDismissRequest(privacyPolicyDialogState) }) {
                            Text(
                                text = stringResource(id = CommonR.string.got_it),
                                style = darkButtonTextStyle
                            )
                        }
                    },

                    ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = CommonR.string.privacy_policy).capitalize(),
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontFamily = fontRoboto,
                                fontSize = 18.sp,
                                letterSpacing = .5.sp
                            )
                        )

                        val mUrl = "https://loremipsum.io/privacy-policy/"

                        AndroidView(factory = {
                            WebView(it).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                webViewClient = WebViewClient()
                                loadUrl(mUrl)
                            }
                        }, update = {
                            it.loadUrl(mUrl)
                        })
                    }
                }
            }

        }
    }
}


@Composable
@Preview
fun PreviewTermsOfUseScreen() {
    ConditionScreen(onBack = {})
}