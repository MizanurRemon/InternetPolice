package com.internetpolice.profile_presentation.password_change

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavHostController
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
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
fun PasswordChangeScreen(
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    viewModel: PasswordChangeViewModel = hiltViewModel(),
) {


    val showCurrentPassword = remember { mutableStateOf(false) }
    val showConfirmNewPassword = remember { mutableStateOf(false) }
    val showConfirmPassword = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->
            when (event) {
                is UiEvent.Success -> {
                    navController.navigate(Route.PASS_UPDATE)
                }

                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short,
                    )
                }

                is UiEvent.NavigateUp -> {
                }
            }

        }
    }
    return Column(
        Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .verticalScroll(rememberScrollState())
    ) {
        AppToolbarCompose(
            title = stringResource(id = CommonR.string.back),
            modifier = Modifier.padding(start = 20.w(), top = 30.h())
        ) {
            onBack()
        }
        Column(
            modifier = Modifier.padding(top = 70.h(), start = 36.w(), end = 36.w()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = CommonR.string.set_your_new_password),
                style = heading2TextStyle.copy(fontSize = 21.ssp()),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 60.w())
            )
            Text(
                text = stringResource(id = CommonR.string.set_new_password_for_internet_police),
                style = bodyRegularTextStyle.copy(color = ColorTextSecondary),
                modifier = Modifier
                    .padding(top = 15.h())
                    .padding(horizontal = 60.w()),
            )

            Text(
                text = stringResource(id = CommonR.string.current_password),
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
                    viewModel.onEvent(PasswordChangeEvent.OnPasswordEnter(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .conditional(state.password.isEmpty()) {
                        return@conditional border(
                            width = 1.dp,
                            color = Color(0xFFE2E4EA),
                            shape = RoundedCornerShape(15.dp)
                        )
                    }
                    .conditional(state.password.isNotEmpty()) {
                        return@conditional border(
                            width = 1.dp, color = ColorError, shape = RoundedCornerShape(15.dp)
                        )
                    },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = if (state.password.isEmpty()) DesignSystemR.drawable.ic_lock else DesignSystemR.drawable.ic_red_error),
                        contentDescription = "",
                        modifier = Modifier.size(24.r())
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = CommonR.string.enter_your_current_password),
                        style = TextStyle(
                            color = ColorTextFieldPlaceholder,
                        )
                    )
                },
                trailingIcon = {
                    Icon(painter = painterResource(id = if (showCurrentPassword.value) DesignSystemR.drawable.ic_eye_slash else DesignSystemR.drawable.ic_eye_unslash),
                        contentDescription = "",
                        tint = ColorTextFieldPlaceholder,
                        modifier = Modifier.noRippleClickable {
                            showCurrentPassword.value = !showCurrentPassword.value
                        })
                },
                visualTransformation = if (showCurrentPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            )
            if (state.isPasswordError) {
                Text(
                    text = context.getString(CommonR.string.enter_your_current_password),
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
                        .padding(top = 4.h(), start = 10.w())
                        .align(Alignment.Start),
                )
            }
            Text(
                text = stringResource(id = CommonR.string.new_password),
                style = subHeadingFormTextStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.h(), bottom = 10.h()),
            )
            TextField(
                value = state.newPassword,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = {
                    viewModel.onEvent(PasswordChangeEvent.OnNewPasswordEnter(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .conditional(state.newPassword.isEmpty()) {
                        return@conditional border(
                            width = 1.dp,
                            color = Color(0xFFE2E4EA),
                            shape = RoundedCornerShape(15.dp)
                        )
                    }
                    .conditional(state.newPassword.isNotEmpty()) {
                        return@conditional border(
                            width = 1.dp, color = ColorError, shape = RoundedCornerShape(15.dp)
                        )
                    }.align( Alignment.CenterHorizontally),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = if (state.newPassword.isEmpty()) DesignSystemR.drawable.ic_lock else DesignSystemR.drawable.ic_red_error),
                        contentDescription = "",
                        modifier = Modifier.size(24.r())
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = CommonR.string.min_8_char),
                        style = TextStyle(
                            color = ColorTextFieldPlaceholder,
                        )
                    )
                },
                trailingIcon = {
                    Icon(painter = painterResource(id = if (showConfirmNewPassword.value) DesignSystemR.drawable.ic_eye_slash else DesignSystemR.drawable.ic_eye_unslash),
                        contentDescription = "",
                        tint = ColorTextFieldPlaceholder,
                        modifier = Modifier.noRippleClickable {
                            showConfirmNewPassword.value = !showConfirmNewPassword.value
                        })
                },
                visualTransformation = if (showConfirmNewPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            )
            if (state.isNewPasswordError) {
                Text(
                    text = context.getString(CommonR.string.invalid_password),
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
                        .padding(top = 4.h(), start = 10.w())
                        .align(Alignment.Start),
                )
            }

            Text(
                text = stringResource(id = CommonR.string.confirm_new_password),
                style = TextStyle(
                    fontFamily = fontRoboto,
                    fontSize = 16.ssp(),
                    color = Color(0xff151B33),
                    fontWeight = FontWeight.W500,
                    lineHeight = 21.ssp(),
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.h(), bottom = 10.h()),
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
                    viewModel.onEvent(PasswordChangeEvent.OnConfirmPasswordEnter(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .conditional(state.confirmPassword.isEmpty()) {
                        return@conditional border(
                            width = 1.dp,
                            color = Color(0xFFE2E4EA),
                            shape = RoundedCornerShape(15.dp)
                        )
                    }
                    .conditional(state.confirmPassword.isNotEmpty()) {
                        return@conditional border(
                            width = 1.dp, color = ColorError, shape = RoundedCornerShape(15.dp)
                        )
                    },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = if (state.confirmPassword.isEmpty()) DesignSystemR.drawable.ic_lock else DesignSystemR.drawable.ic_red_error),
                        contentDescription = "",
                        modifier = Modifier.size(24.r())
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = CommonR.string.min_8_char), style = TextStyle(
                            color = ColorTextFieldPlaceholder,
                        )
                    )
                },
                trailingIcon = {
                    Icon(painter = painterResource(id = if (showConfirmPassword.value) DesignSystemR.drawable.ic_eye_slash else DesignSystemR.drawable.ic_eye_unslash),
                        contentDescription = "",
                        tint = ColorTextFieldPlaceholder,
                        modifier = Modifier.noRippleClickable {
                            showConfirmPassword.value = !showConfirmPassword.value
                        })
                },
                visualTransformation = if (showConfirmPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            )
            if (state.isConfirmPasswordError) {
                Text(
                    text = context.getString(CommonR.string.invalid_password),
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
                        .padding(top = 4.h(), start = 10.w())
                        .align(Alignment.Start),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(1f))
        AppActionButtonCompose(
            stringId = CommonR.string.continues,
            modifier = Modifier.padding(start = 36.w(), end = 36.w())
        ) {
            viewModel.onEvent(PasswordChangeEvent.OnSubmitClickForPasswordReset)
        }
        Spacer(modifier = Modifier.height(54.h()))

        if (state.isShowDialog) LoadingDialog {}
    }
}

@Composable
@DevicePreviews
fun PreviewSetNewPassScreen() {
//    PasswordChangeScreen(onBack = {
//
//    })
}
