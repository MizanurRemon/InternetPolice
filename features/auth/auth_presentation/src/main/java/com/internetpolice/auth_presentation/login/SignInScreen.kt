package com.internetpolice.auth_presentation.login

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSignUp: () -> Unit
) {
    val isSignIn = true
    val showPassword = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val noteText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Gray, fontWeight = FontWeight.W300)) {
            append(stringResource(id = CommonR.string.donot_have_account) + " ")
        }

        pushStringAnnotation(
            tag = CommonR.string.sign_up_for_free.toString(),
            annotation = CommonR.string.sign_up_for_free.toString()
        )

        withStyle(style = SpanStyle(color = ColorPrimaryDark, fontWeight = FontWeight.W700)) {
            append(stringResource(id = CommonR.string.sign_up_for_free).replace(".", ""))
        }

        append(".")
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {

                    if (viewModel.state.isDraftUser) {
                        navController.navigate(Route.VERIFY_OTP_EMAIL + "/${viewModel.state.email}" + "/${Route.SIGN_IN}")
                    } else {
                        navController.navigate(Route.NOTIFICATION_ON_OFF + "/${isSignIn}") {
                            popUpTo(navController.graph.id)
                        }
                    }

                }

                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
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
            text = stringResource(id = CommonR.string.welcome),
            style = heading2TextStyle,
        )
        Text(
            text = stringResource(id = CommonR.string.sign_in_subtitle),
            style = TextStyle(
                fontFamily = fontRoboto,
                fontSize = 16.ssp(),
                color = ColorTextSecondary,
                fontWeight = FontWeight.W300,
                lineHeight = 25.ssp(),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 30.h(), start = 20.w(), end = 20.w()),
        )
        Text(
            text = stringResource(id = CommonR.string.email),
            style = TextStyle(
                fontFamily = fontRoboto,
                fontSize = 16.ssp(),
                color = Color(0xff151B33),
                fontWeight = FontWeight.W500,
                lineHeight = 21.ssp(),
                textAlign = TextAlign.Left
            ),
            modifier = Modifier
                .padding(top = 53.h(), bottom = 10.h())
                .align(Alignment.Start),
        )
        TextField(
            singleLine = true,
            value = viewModel.state.email,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = {
                viewModel.onEvent(LoginEvent.OnEmailEnter(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .onFocusChanged {
                    viewModel.isEmailValid()
                }
                .conditional(viewModel.state.isEmailValid) {
                    return@conditional border(
                        width = 1.dp,
                        color = Color(0xFFE2E4EA),
                        shape = RoundedCornerShape(15.dp)
                    )
                }
                .conditional(!viewModel.state.isEmailValid) {
                    return@conditional border(
                        width = 1.dp,
                        color = ColorError,
                        shape = RoundedCornerShape(15.dp)
                    )
                },
            leadingIcon = {
                Image(
                    painter = painterResource(id = if (viewModel.state.isEmailValid) DesignSystemR.drawable.ic_email else DesignSystemR.drawable.ic_red_error),
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
        if (!viewModel.state.isEmailValid) {
            Text(
                text = context.getString(CommonR.string.invalid_email),
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = CommonR.string.password),
                style = TextStyle(
                    fontFamily = fontRoboto,
                    fontSize = 16.sp,
                    color = Color(0xff151B33),
                    fontWeight = FontWeight.W500,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Left
                ),
            )
            Text(
                text = stringResource(id = CommonR.string.forget_password_exla),
                style = TextStyle(
                    fontFamily = fontRoboto,
                    fontSize = 16.sp,
                    color = ColorTextPrimary,
                    fontWeight = FontWeight.W300,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Right
                ),
                modifier = Modifier.clickable {
                    navController.navigate(Route.FORGET_PASS_EMAIL_INPUT)
                }
            )
        }
        TextField(
            singleLine = true,
            value = viewModel.state.password,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                viewModel.onEvent(LoginEvent.OnSubmitClick)
                defaultKeyboardAction(ImeAction.Done)
            }),
            onValueChange = {
                viewModel.onEvent(LoginEvent.OnPasswordEnter(it))
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .conditional(viewModel.state.isPasswordValid) {
                    return@conditional border(
                        width = 1.dp,
                        color = Color(0xFFE2E4EA),
                        shape = RoundedCornerShape(15.dp)
                    )
                }
                .conditional(!viewModel.state.isPasswordValid) {
                    return@conditional border(
                        width = 1.dp,
                        color = ColorError,
                        shape = RoundedCornerShape(15.dp)
                    )
                },
            leadingIcon = {
                Image(
                    painter = painterResource(id = if (viewModel.state.isPasswordValid) DesignSystemR.drawable.ic_lock else DesignSystemR.drawable.ic_red_error),
                    contentDescription = "",
                    modifier = Modifier.size(24.r())
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

        if (!viewModel.state.isPasswordValid) {
            Text(
                text = context.getString(CommonR.string.invalid_password),
                style = TextStyle(
                    fontWeight = FontWeight.W300,
                    fontFamily = fontRoboto,
                    lineHeight = 24.sp,
                    fontSize = 15.sp,
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
        AppActionButtonCompose(stringId = CommonR.string.sign_in) {
            viewModel.onEvent(LoginEvent.OnSubmitClick)
        }


        ClickableText(
            modifier = Modifier.padding(top = 40.h(), bottom = 55.h()),
            text = noteText,
            style = bodyRegularTextStyle.copy(
                color = ColorTextPrimary,
                // letterSpacing = .5.sp,
                textAlign = TextAlign.Center
            ), onClick = { offset ->
                noteText.getStringAnnotations(
                    tag = CommonR.string.sign_up_for_free.toString(),
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    viewModel.signUpAvailabilityCheck(navController)
                }
            }
        )

        BackHandler {
            onBack()
        }

        if (viewModel.state.isShowDialog)
            LoadingDialog {}
    }
}