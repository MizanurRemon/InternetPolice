package com.internetpolice.settings_presentation.screens.about_us

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.internetpolice.core.common.R
import com.internetpolice.core.common.util.DEFAULT_LANGUAGE_TAG
import com.internetpolice.core.common.util.openExternalLink
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.languageList
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.settings_presentation.screens.about_us.AboutUsViewModel
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun AboutUsScreen(onBack: () -> Unit, viewModel: AboutUsViewModel = hiltViewModel()) {

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .padding(start = 16.w(), top = 24.h(), end = 16.w())

    ) {
        AppToolbarCompose(title = stringResource(id = R.string.back)) {
            onBack()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.w(), end = 30.w(), bottom = 55.h())
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = if (viewModel.state.tag == languageList[1].tag) {
                    painterResource(id = DesignSystemR.drawable.ic_logo_horizontal_dutch)
                } else {
                    painterResource(id = DesignSystemR.drawable.ic_logo_horizontal)
                },
                modifier = Modifier
                    .padding(start = 30.w(), end = 30.w())
                    .padding(vertical = 30.h())
                    .fillMaxWidth(),
                   // .height(60.h()),
                contentDescription = ""
            )

            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_cop_intro),
                modifier = Modifier.height(230.dp),
                contentDescription = ""
            )


            Text(
                text = stringResource(id = CommonR.string.about_us),
                style = subHeading1TextStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.h(), bottom = 20.h())
            )

            Text(
                text = stringResource(id = CommonR.string.about_us_content_1),
                style = bodyRegularTextStyle.copy(
                  //  color = ColorTextSecondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.w())
            )

            Text(
                text = stringResource(id = CommonR.string.about_us_content_2),
                style = bodyRegularTextStyle.copy(
                   // color = ColorTextSecondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.w())
            )

            Spacer(modifier = Modifier.weight(1f))


            AppActionButtonCompose(
                stringId = CommonR.string.read_more,
                modifier = Modifier.padding()
            ) {

                val url = when (viewModel.state.tag) {
                    DEFAULT_LANGUAGE_TAG -> {
                        "https://internetpolitie.nl/over-ons/"
                    }

                    else -> {
                        "https://internetpolice.com/about-us/"
                    }
                }

                openExternalLink(url = url, context = context)

            }

        }

    }
}

@Composable
@DevicePreviews
fun PreviewAboutScreen() {
    AboutUsScreen(onBack = {})
}