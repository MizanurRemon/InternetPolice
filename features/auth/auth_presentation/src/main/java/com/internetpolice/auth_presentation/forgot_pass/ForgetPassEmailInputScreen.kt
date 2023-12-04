package com.internetpolice.auth_presentation.forgot_pass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPassEmailInput(
    navController: NavController,
    snackBarHostState: SnackbarHostState,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->

            when (event) {
                is UiEvent.Success -> {
                    navController.navigate(Route.FORGET_PASS_CHECK_YOUR_MAIL+"/${viewModel.state.email}")
                }
                is UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short,
                    )
                }
                is UiEvent.NavigateUp -> {
                    navController.navigateUp()
                }
            }

        }
    }
    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .padding(top = 80.h(), start = 35.w(), end = 35.w()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = CommonR.string.ops),
            style = heading2TextStyle,
        )
        Text(
            text = stringResource(id = CommonR.string.forget_password_let_us_send_email),
            style = bodyRegularTextStyle.copy(color = ColorTextSecondary),
            modifier = Modifier.padding(top = 30.h(), start = 20.w(), end = 20.w()),
        )
        Text(
            text = stringResource(id = CommonR.string.please_enter_email_address),
            style = subHeadingFormTextStyle,
            modifier = Modifier
                .padding(top = 53.h(), bottom = 10.h())
                .align(Alignment.Start),
        )
        TextField(
            value = viewModel.state.email,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = {
                viewModel.onEvent(ForgotPasswordEvent.OnEmailEnter(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .conditional(!viewModel.state.isError) {
                    return@conditional border(
                        width = 1.dp,
                        color = Color(0xFFE2E4EA),
                        shape = RoundedCornerShape(15.dp)
                    )
                }
                .conditional(viewModel.state.isError) {
                    return@conditional border(
                        width = 1.dp,
                        color = ColorError,
                        shape = RoundedCornerShape(15.dp)
                    )
                },
            leadingIcon = {
                Image(
                    painter = painterResource(id = if (!viewModel.state.isError) DesignSystemR.drawable.ic_email else DesignSystemR.drawable.ic_red_error),
                    contentDescription = "",
                    modifier = Modifier.size(24.r())
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = CommonR.string.enter_your_email), style = TextStyle(
                        color = ColorTextFieldPlaceholder,
                    )
                )
            },
        )
        if (viewModel.state.isError) {
            Text(
                text = stringResource(id = CommonR.string.invalid_email),
                style = TextStyle(
                    fontWeight = FontWeight.W300,
                    fontFamily = fontRoboto,
                    lineHeight = 24.ssp(),
                    fontSize = 15.ssp(),
                    textAlign = TextAlign.Left,
                    color = ColorError
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.h())
                    .align(Alignment.Start),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        AppActionButtonCompose(stringId = CommonR.string.send) {
            viewModel.onEvent(ForgotPasswordEvent.OnSubmitClickForLinkSend)

        }
        Spacer(modifier = Modifier.height(55.h()))
        if (viewModel.state.isShowDialog)
            LoadingDialog {}
    }
}

@DevicePreviews
@Composable
fun PreviewForgetPassEmailInput() {
//    ForgetPassEmailInput {
//
//    }
}
