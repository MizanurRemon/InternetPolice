package com.internetpolice.auth_presentation.forgot_pass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPassSetNewPassScreen(
    email: String,
    navController: NavController,
    snackBarHostState: SnackbarHostState,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
) {

    val showPassword = remember { mutableStateOf(false) }
    val showConfirmPassword = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val state = viewModel.state
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->

            when (event) {
                is UiEvent.Success -> {
                    navController.navigate(Route.FORGET_PASS_UPDATE)
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
            .padding(top = 130.h(), start = 36.w(), end = 36.w()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = CommonR.string.set_your_new_password),
            style = heading1TextStyle,
            textAlign = TextAlign.Start
        )
        Text(
            text = stringResource(id = CommonR.string.please_set_your_new_password),
            style = bodyRegularTextStyle.copy(color = ColorTextSecondary),
            modifier = Modifier.padding(top = 15.h()),
        )

        Text(
            text = stringResource(id = CommonR.string.password),
            style = subHeadingFormTextStyle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.h(), bottom = 10.h()),
        )
        TextField(
            value = state.password,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {
                viewModel.onEvent(ForgotPasswordEvent.OnPasswordEnter(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .conditional(!state.isPasswordError) {
                    return@conditional border(
                        width = 1.dp,
                        color = Color(0xFFE2E4EA),
                        shape = RoundedCornerShape(15.dp)
                    )
                }
                .conditional(state.isPasswordError) {
                    return@conditional border(
                        width = 1.dp,
                        color = ColorError,
                        shape = RoundedCornerShape(15.dp)
                    )
                },
            leadingIcon = {
                Image(
                    painter = painterResource(id = if (!state.isPasswordError) DesignSystemR.drawable.ic_lock else DesignSystemR.drawable.ic_red_error),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = CommonR.string.enter_your_password),
                    style = TextStyle(
                        color = ColorTextFieldPlaceholder,
                    )
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (showPassword.value) DesignSystemR.drawable.ic_eye_slash else DesignSystemR.drawable.ic_eye_unslash),
                    contentDescription = "", tint = ColorTextFieldPlaceholder,
                    modifier = Modifier.clickable {
                        showPassword.value = !showPassword.value
                    }
                )
            },
            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        )
        if (state.isPasswordError) {
            Text(
                text = stringResource(id = CommonR.string.invalid_password),
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

        Text(
            text = stringResource(id = CommonR.string.confirm_password),
            style = subHeadingFormTextStyle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.h(), bottom = 10.h()),
        )
        TextField(
            value = state.confirmPassword,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {
                viewModel.onEvent(ForgotPasswordEvent.OnConfirmPasswordEnter(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .conditional(!state.isConfirmPasswordError) {
                    return@conditional border(
                        width = 1.dp,
                        color = Color(0xFFE2E4EA),
                        shape = RoundedCornerShape(15.dp)
                    )
                }
                .conditional(state.isConfirmPasswordError) {
                    return@conditional border(
                        width = 1.dp,
                        color = ColorError,
                        shape = RoundedCornerShape(15.dp)
                    )
                },
            leadingIcon = {
                Image(
                    painter = painterResource(id = if (!state.isConfirmPasswordError) DesignSystemR.drawable.ic_lock else DesignSystemR.drawable.ic_red_error),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = CommonR.string.retype_your_password),
                    style = TextStyle(
                        color = ColorTextFieldPlaceholder,
                    )
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (showConfirmPassword.value) DesignSystemR.drawable.ic_eye_slash else DesignSystemR.drawable.ic_eye_unslash),
                    contentDescription = "", tint = ColorTextFieldPlaceholder,
                    modifier = Modifier.clickable {
                        showConfirmPassword.value = !showConfirmPassword.value
                    }
                )
            },
            visualTransformation = if (showConfirmPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        )
        if (state.isConfirmPasswordError) {
            Text(
                text = stringResource(id = CommonR.string.invalid_password),
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
        AppActionButtonCompose(stringId = CommonR.string.continues) {
            viewModel.onEvent(ForgotPasswordEvent.OnSubmitClickForPasswordReset)
        }
        Spacer(modifier = Modifier.height(54.h()))

        if (state.isShowDialogForPasswordReset)
            LoadingDialog {}
    }
}

@Composable
@DevicePreviews
fun PreviewForgetPassSetNewPassScreen() {
//    ForgetPassSetNewPassScreen {
//
//    }
}