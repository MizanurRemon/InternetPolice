package com.internetpolice.settings_presentation.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.settings_presentation.helpers.ScreenSize
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportSuccessScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            AppToolbarCompose(
                title = stringResource(id = CommonR.string.back),
                modifier = Modifier.padding(start = 16.w(), top = 24.h(), end = 16.w())
            ) {
                onBack()
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            AppActionButtonCompose(
                stringId = CommonR.string.done,
                modifier = Modifier.padding(vertical = 55.h(), horizontal = 36.w())
            ) {
                onBack()
            }
        },
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.h())
                .background(brush = AppBrush),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            Image(
                painterResource(id = DesignSystemR.drawable.female_police_left_side_green_check_thumbs_up),
                modifier = Modifier
                    .size((ScreenSize.height() / 3).dp),
                contentDescription = ""
            )

            Text(
                text = stringResource(id = CommonR.string.we_highly_appreciate_your_input),
                style = heading1TextStyle,
                modifier = Modifier.padding(horizontal = 56.w(), vertical = 30.h()),
                textAlign = TextAlign.Center
            )

        }
    }

    BackHandler {
        onBack()
    }
}

@Composable
@DevicePreviews
fun PreviewReportSuccess() {
    ReportSuccessScreen(onBack = {})
}