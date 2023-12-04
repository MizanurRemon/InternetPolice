package com.internetpolice.onboardings_presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews

@Composable
fun OnBoardingPersonaliseProfileScreen(
    onGoAvatar: () -> Unit,
    onGoHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(brush = AppBrush),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = CommonR.string.great),
            style = heading1TextStyle,
            modifier = Modifier.padding(top = 30.h())
        )
        Text(
            text = stringResource(id = CommonR.string.you_are_almost_ready),
            style = bodyRegularTextStyle.copy(color = ColorTextSecondary),
            modifier = Modifier.padding(top = 20.h())
        )
        Image(
            painter = painterResource(id = DesignSystemR.drawable.ic_onboard_personalise),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(230.h())
        )
        Text(
            text = stringResource(id = CommonR.string.personalise_your_profile),
            modifier = Modifier.padding(top = 20.h()),
            style = subHeading1TextStyle
        )
        Text(
            text = stringResource(id = CommonR.string.info_profile_personalise),
            style = bodyRegularTextStyle.copy(color = ColorTextSecondary),
            modifier = Modifier.padding(top = 20.h(), start = 40.w(), end = 40.w())
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .padding(start = 32.w(), end = 32.w(), top = 20.h(), bottom = 54.h())
        ) {
            Column {
                AppActionButtonCompose(
                    stringId = CommonR.string.maybe_later,
                    textColor = ColorPrimaryDark,
                    bgColor = Color.White,
                    borderColor = ColorPrimaryDark
                ) {
                    onGoHome()
                }
                Spacer(modifier = Modifier.height(24.h()))
                AppActionButtonCompose(
                    stringId = CommonR.string.ok_let_go,
                    bgColor = ColorPrimaryDark,
                    textColor = Color.White
                ) {
                    onGoAvatar()
                }
            }

        }
    }
}

@DevicePreviews
@Composable
fun previewOnBoardingPersonaliseProfileScreen() {
    OnBoardingPersonaliseProfileScreen(onGoHome = {}, onGoAvatar = {})
}