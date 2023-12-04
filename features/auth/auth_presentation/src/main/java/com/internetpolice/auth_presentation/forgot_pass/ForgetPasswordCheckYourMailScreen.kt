package com.internetpolice.auth_presentation.forgot_pass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.ColorTextSecondary
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.heading2TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun ForgetPasswordCheckYourMailScreen(email: String, onGotIt: (String) -> Unit) {

    LocalContext.current;

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .padding(top = 130.h(), start = 35.w(), end = 35.w())
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = CommonR.string.check_you_email),
            style = heading2TextStyle,
        )
        Text(
            text = stringResource(id = CommonR.string.we_have_send_you_a_reset_link),
            style = bodyRegularTextStyle.copy(color = ColorTextSecondary),
            modifier = Modifier.padding(top = 15.h()),
        )
        Image(
            painter = painterResource(id = DesignSystemR.drawable.ic_verify_email),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 110.h(),)
                .size(200.r())
        )
        Spacer(modifier = Modifier.weight(1f))
        AppActionButtonCompose(stringId = CommonR.string.got_it) {
            onGotIt(email)
        }
        Spacer(modifier = Modifier.height(55.h()))
    }
}

@Composable
@DevicePreviews
fun PreviewForgetPasswordCheckYourMailScreen() {
    ForgetPasswordCheckYourMailScreen(email = "",) {
    }
}

