package com.internetpolice.profile_presentation.email_update

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews

@Composable
fun EmailUpdatedScreen(
    onFinished: () -> Unit,
) {

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .padding(top = 100.h(), start = 35.w(), end = 35.w())
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = DesignSystemR.drawable.ic_cop_verified),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 20.h())
        )
        Text(
            text = stringResource(id = CommonR.string.email_updated),
            style = heading2TextStyle,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = CommonR.string.email_updated_info),
            style = bodyRegularTextStyle.copy(color = ColorTextSecondary),
            modifier = Modifier.padding(top = 15.h()),
        )
        Spacer(modifier = Modifier.weight(1f))
        AppActionButtonCompose(
            stringId = CommonR.string.done,
            modifier = Modifier.padding(bottom = 55.h())
        ) {
            onFinished()
        }

        BackHandler {
            onFinished()
        }
    }
}

@Composable
@DevicePreviews
fun PreviewEmailUpdatedScreen() {
//    EmailUpdatedScreen(onFinished = {
//
//    })
}

