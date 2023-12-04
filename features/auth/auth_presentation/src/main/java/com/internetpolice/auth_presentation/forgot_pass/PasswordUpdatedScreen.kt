package com.internetpolice.auth_presentation.screens

import androidx.annotation.StringRes
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
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.ColorTextSecondary
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.heading2TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun PasswordUpdatedScreen(@StringRes actionTextResId: Int, onSubmit: () -> Unit) {
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
            text = stringResource(id = CommonR.string.password_updated),
            style = heading2TextStyle,
        )
        Text(
            text = stringResource(id = CommonR.string.your_new_password_is_set),
            style = bodyRegularTextStyle.copy(color = ColorTextSecondary),
            modifier = Modifier.padding(top = 15.h()),
        )
        Spacer(modifier = Modifier.weight(1f))
        AppActionButtonCompose(stringId = actionTextResId) {
            onSubmit()
        }
        Spacer(modifier = Modifier.height(54.h()))

    }
}

@DevicePreviews
@Composable
fun PreviewPasswordUpdatedScreen() {
    PasswordUpdatedScreen(actionTextResId = CommonR.string.done) {

    }
}
