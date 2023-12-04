package com.internetpolice.app.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.InfoItemCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.ColorPrimaryDark
import com.internetpolice.core.designsystem.theme.heading1TextStyle
import com.internetpolice.core.designsystem.theme.subHeading1TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import kotlinx.coroutines.delay
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR


@Composable
fun GuestPreAccountCreationInfoScreen(
    onFinished: () -> Unit,
    onAccountCreateButtonClick: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        delay(4000L)
        onFinished()
    }
    return Column(
        modifier = Modifier
            .background(brush = AppBrush)
            .fillMaxSize()
            .padding(start = 36.w(), end = 36.w(), bottom = 55.h(), top = 60.h()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = CommonR.string.wait_guest_title),
            style = subHeading1TextStyle.copy(color = Color(0xff999999)),
            modifier = Modifier.padding(
                start = 36.w(),
                end = 36.w(),
            )
        )
        Spacer(modifier = Modifier.height(40.h()))
        DotsFlashingCompose()
        Spacer(modifier = Modifier.height(40.h()))
        Image(
            painter = painterResource(id = DesignSystemR.drawable.ic_cop_question_guest),
            contentDescription = null,
            modifier = Modifier
                .size(345.r()),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = "Guests are missing out!",
            style = heading1TextStyle,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        InfoItemCompose(
            titleStringResId = CommonR.string.no_website_report,
            iconResId = DesignSystemR.drawable.ic_negative
        )
        InfoItemCompose(
            titleStringResId = CommonR.string.no_gaining_experience,
            iconResId = DesignSystemR.drawable.ic_negative
        )
        InfoItemCompose(
            titleStringResId = CommonR.string.no_personal_avatar,
            iconResId = DesignSystemR.drawable.ic_negative
        )
        InfoItemCompose(
            titleStringResId = CommonR.string.no_access_premium_feature,
            iconResId = DesignSystemR.drawable.ic_negative
        )
        Spacer(
            modifier = Modifier
                .padding(top = 44.h())
                .weight(1f)
        )
        AppActionButtonCompose(
            stringId = CommonR.string.let_create_an_account,
            bgColor = ColorPrimaryDark, textColor = Color.White
        ) {
            onAccountCreateButtonClick()
        }
    }
}


@Composable
fun DotsFlashingCompose() {
    val dotSize = 12.r()
    val delayUnit = 300 // you can change delay to change animation speed
    val minAlpha = 0.1f

    @Composable
    fun Dot(
        alpha: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .alpha(alpha)
            .background(
                color = ColorPrimaryDark,
                shape = CircleShape
            )
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateAlphaWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = minAlpha,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                minAlpha at delay with LinearEasing
                1f at delay + delayUnit with LinearEasing
                minAlpha at delay + delayUnit * 2
            }
        )
    )

    val alpha1 by animateAlphaWithDelay(0)
    val alpha2 by animateAlphaWithDelay(delayUnit)
    val alpha3 by animateAlphaWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val spaceSize = 9.w()
        Dot(alpha1)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha2)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha3)
    }
}

@Composable
@DevicePreviews
fun PreGuestPreAccountCreationInfoScreen() {
    GuestPreAccountCreationInfoScreen(onFinished = {}, onAccountCreateButtonClick = {})
}

