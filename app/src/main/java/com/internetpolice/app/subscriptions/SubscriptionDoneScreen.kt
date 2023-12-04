package com.internetpolice.app.subscriptions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.heading1TextStyle
import com.internetpolice.core.designsystem.w

@Composable
fun SubscriptionDoneScreen(onFinish: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBrush),
        contentAlignment = Alignment.TopCenter
    ) {
        Column() {
            Image(
                painter =
                painterResource(id = DesignSystemR.drawable.ic_subscription_done),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .padding(top = 100.h())
            )
            Text(
                text =
                stringResource(id = CommonR.string.enjoy) + "!",
                style = heading1TextStyle
                    .copy(textAlign = TextAlign.Center),
                modifier = Modifier
                    .padding(vertical = 40.h())
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(
                    id =
                    CommonR.string.enjoy_news_internet_protection
                ),
                style = bodyRegularTextStyle.copy(
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()

            )
            Spacer(modifier = Modifier.weight(1f))
            AppActionButtonCompose(
                stringId = CommonR.string.ok_let_go,
                modifier = Modifier
                    .padding(vertical = 55.h(), horizontal = 34.w())
                    .fillMaxWidth()
            ) {
                onFinish()
            }
        }
    }
}
