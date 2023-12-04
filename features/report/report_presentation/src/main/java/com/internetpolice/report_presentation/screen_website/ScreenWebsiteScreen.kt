package com.internetpolice.report_presentation.screen_website

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.ColorPrimaryDark
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.heading1TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR


@Composable
fun ScreenWebsiteScreen(
    onSubmit: () -> Unit, onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(brush = AppBrush)
            .padding(top = 30.h(), start = 16.w(), end = 16.w(), bottom = 55.h())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppToolbarCompose(title = stringResource(id = CommonR.string.home),
                onBackClick = {
                    onBack()
                })
            Spacer(modifier = Modifier.height(135.h()))
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_address_search),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(40.h()))
            Text(
                text = stringResource(id = CommonR.string.check_a_website),
                style = heading1TextStyle
            )
            Spacer(modifier = Modifier.height(20.h()))
            Text(
                text = stringResource(id = CommonR.string.check_a_website_info),
                style = bodyRegularTextStyle,
            )
            Spacer(modifier = Modifier.weight(1f))
            AppActionButtonCompose(
                stringId = CommonR.string.get_started,
                bgColor = ColorPrimaryDark,
                textColor = Color.White,
                modifier = Modifier.padding(start = 16.w(), end = 16.w())
            ) {
                onSubmit()
            }


        }
    }
}

@Preview
@Composable
fun PreviewScreenWebsiteScreen() {
    ScreenWebsiteScreen(
        onBack = {},
        onSubmit = {}
    )
}