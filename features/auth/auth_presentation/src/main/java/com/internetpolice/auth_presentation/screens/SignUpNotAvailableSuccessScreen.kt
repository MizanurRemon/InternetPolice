package com.internetpolice.auth_presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.internetpolice.core.common.util.FACEBOOK_URL
import com.internetpolice.core.common.util.INSTAGRAM_URL
import com.internetpolice.core.common.util.LINKEDIN_URL
import com.internetpolice.core.common.util.TWITTER_URL
import com.internetpolice.core.common.util.openExternalLink
import com.internetpolice.core.designsystem.R
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.BORDER_COLOR
import com.internetpolice.core.designsystem.theme.SOCIAL_ICON_BORDER_C0LOR
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.subHeading1TextStyle
import com.internetpolice.core.designsystem.w

@Composable
fun SignUpNotAvailableSuccessScreen(onDone: () -> Unit) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = AppBrush)
                .padding(
                    top = 130.h(), bottom = 55.h(), start = 40.w(), end = 40.w()
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_large_green_check),
                contentDescription = "",
                modifier = Modifier.size(140.w())
            )
            Spacer(modifier = Modifier.height(20.h()))

            Text(
                text = stringResource(
                    id = com.internetpolice.core.common.R.string.thank_you_for_providing_email
                ), style = subHeading1TextStyle,
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(20.h()))

            Text(
                text = stringResource(
                    id = com.internetpolice.core.common.R.string.first_to_know
                ),
                style = bodyRegularTextStyle,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(20.h()))

            Text(
                text = stringResource(
                    id = com.internetpolice.core.common.R.string.stay_connected
                ),
                style = bodyRegularTextStyle,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(20.h()))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.w(), end = 30.w())
            ) {

                RowIconItem(DesignSystemR.drawable.ic_facebook, onClick = {
                    openExternalLink(url = FACEBOOK_URL, context = context)
                })

                RowIconItem(DesignSystemR.drawable.ic_instagram, onClick = {
                    openExternalLink(url = INSTAGRAM_URL, context = context)
                })

                RowIconItem(DesignSystemR.drawable.ic_inbanx, onClick = {
                    openExternalLink(url = TWITTER_URL, context = context)
                })

                RowIconItem(DesignSystemR.drawable.ic_linkedin, onClick = {
                    openExternalLink(url = LINKEDIN_URL, context = context)
                })

            }

            Spacer(modifier = Modifier.weight(1f))

            AppActionButtonCompose(
                stringId = com.internetpolice.core.common.R.string.done, modifier = Modifier
            ) {
                onDone()
            }


        }

        Row(
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .padding(top = 40.h(), end = 40.w())
        ) {
            Image(painter = painterResource(id = DesignSystemR.drawable.ic_blue_cross),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        onDone()
                    })
        }
    }

}

@Composable
fun RowIconItem(image: Int, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .background(color = Color.White, shape = CircleShape)
            .border(width = 3.dp, color = SOCIAL_ICON_BORDER_C0LOR, shape = CircleShape)
            .clickable {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "",
            modifier = Modifier
                .size(60.h())
                .padding(15.dp)
        )
    }
}

@Preview
@Composable
fun PreViewRowIconItem() {
    RowIconItem(R.drawable.ic_facebook, onClick = {})
}


@Composable
fun PreviewSignUpNotAvailableSuccessScreen() {
    SignUpNotAvailableSuccessScreen(onDone = {})
}