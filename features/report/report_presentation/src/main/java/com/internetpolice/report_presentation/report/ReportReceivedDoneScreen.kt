package com.internetpolice.report_presentation.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews

@Composable
fun ReportReceivedDoneScreen(website: String, onFinished: () -> Unit) {
    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .padding(start = 35.w(), end = 35.w(), top = 120.h())
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = DesignSystemR.drawable.ic_report_received_female),
            contentDescription = null,
            modifier = Modifier.wrapContentHeight()
        )
        Text(
            text = stringResource(id = CommonR.string.report_received),
            style = heading2TextStyle,
        )
        Spacer(modifier = Modifier.height(20.h()))
        Text(
            text = website + " " + stringResource(id = CommonR.string.has_been_reported),
            style = bodyBoldTextStyle,
        )
        Spacer(modifier = Modifier.height(20.h()))
        Text(
            text = stringResource(id = CommonR.string.report_received_info),
            style = bodyRegularTextStyle.copy(color = ColorTextSecondary),
            modifier = Modifier.padding(top = 15.h()),
        )
        Spacer(modifier = Modifier.weight(1f))
        AppActionButtonCompose(
            stringId = CommonR.string.continues,
            modifier = Modifier.padding(bottom = 55.h())
        ) {
            onFinished()
        }
        Spacer(modifier = Modifier.height(55.h()))
    }
}

@Composable
@DevicePreviews
fun PreviewReportReceivedDoneScreen() {
    ReportReceivedDoneScreen(
        website = "www.github.com",
        onFinished = {

        })
}

