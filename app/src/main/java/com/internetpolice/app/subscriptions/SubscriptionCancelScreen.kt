package com.internetpolice.app.subscriptions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.bodyBoldTextStyle
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.heading1TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews


@Composable
fun SubscriptionCancelScreen(onBack: () -> Unit) {
    return Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.w(), end = 20.w(), top = 30.h())
        ) {
            AppToolbarCompose(title = stringResource(id = CommonR.string.home)) {
                onBack()
            }
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_cops_unsubscribe),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 36.h()),
                contentScale = ContentScale.Crop
            )
            Text(
                text = stringResource(id = CommonR.string.your_subs_cancel),
                style = heading1TextStyle,
                modifier = Modifier.padding(horizontal = 16.w())
            )
            Spacer(modifier = Modifier.height(20.h()))
            Text(
                text = stringResource(id = CommonR.string.your_subs_cancel_info),
                style = bodyRegularTextStyle,
                modifier = Modifier.padding(horizontal = 16.w())
            )
            Text(
                text = stringResource(id = CommonR.string.let_us_know_why),
                style = bodyBoldTextStyle.copy(
                    color = Color(0xff3E6FCC),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(horizontal = 16.w())
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
            Spacer(modifier = Modifier.height(50.r()))
            AppActionButtonCompose(stringId = CommonR.string.done) {
                onBack()
            }
            Spacer(modifier = Modifier.height(55.h()))


        }

    }
}

@Composable
@DevicePreviews
fun PreviewSubscriptionCancelScreen() {
    SubscriptionCancelScreen(onBack = {})
}