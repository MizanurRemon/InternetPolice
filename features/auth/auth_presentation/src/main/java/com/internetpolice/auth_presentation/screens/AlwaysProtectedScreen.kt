package com.internetpolice.auth_presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.designScreenHeight
import com.internetpolice.core.designsystem.designScreenWidth
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.ColorTextSecondary
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.heading1TextStyle
import com.internetpolice.core.designsystem.theme.subHeading1TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun AlwaysProtectedScreen(onContinue: () -> Unit) {


    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .padding(
                top = 112.h(),
                bottom = 60.h()
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(modifier = Modifier.offset((designScreenWidth / 12.23).dp)) {
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_always_protected),
                contentDescription = "",
                modifier = Modifier
                    .height((designScreenHeight / 3.25).dp)
            )
        }

        Spacer(modifier = Modifier.height(50.h()))

        Text(
            text = stringResource(
                id = CommonR.string.always_protected
            ),
            style = heading1TextStyle.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.W700,
                lineHeight = 33.ssp(),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(
                start = 36.w(),
                end = 36.w(),
            )
        )

        Spacer(modifier = Modifier.height(20.h()))

        Text(
            text = stringResource(
                id = CommonR.string.always_protected_text1
            ),
            style = bodyRegularTextStyle.copy(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.W400,
                color = ColorTextSecondary,
                fontSize = 16.ssp(),
                lineHeight = 25.ssp(),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(
                start = 36.w(),
                end = 36.w(),
            ),
            //letterSpacing = .8.sp
        )

        Spacer(modifier = Modifier.height(20.h()))

        Text(
            text = stringResource(id = CommonR.string.always_protected_text2),
            style = bodyRegularTextStyle.copy(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.W400,
                color = ColorTextSecondary,
                fontSize = 16.ssp(),
                lineHeight = 25.ssp(),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(
                start = 36.w(),
                end = 36.w(),
            ),
            //letterSpacing = .8.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        AppActionButtonCompose(
            stringId = CommonR.string.continues,
            modifier = Modifier.padding(
                start = 36.w(),
                end = 36.w(),
            )
        ) {
            onContinue()
        }

    }
}


@DevicePreviews
//@Preview
@Composable
fun PreviewAlwaysProtectedScreen() {
    AlwaysProtectedScreen(onContinue = {})
}

