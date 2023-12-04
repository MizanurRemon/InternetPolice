package com.internetpolice.auth_presentation.registration

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.AGE_LIST
import com.internetpolice.core.common.util.TERMS_SERVICE_URL
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.openExternalLink
import com.internetpolice.core.designsystem.components.AgeGroupComposeSignup
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    snackBarHostState: SnackbarHostState,
    navController: NavController,
    redirectVerifyScreen: (email: String, source: String) -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val context = LocalContext.current

    val showPassword = remember { mutableStateOf(false) }
    val showConfirmPassword = remember { mutableStateOf(false) }
    val selectedAgeCategoryItem = remember { mutableStateOf("60+") }

    val mail = remember {
        mutableStateOf("")
    }


    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->

            when (event) {
                is UiEvent.Success -> {
                    redirectVerifyScreen(mail.value, Route.SIGN_UP)
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

    val termsConditions = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = ColorTextSecondary, fontSize = 16.sp
            )
        ) {
            append(stringResource(id = CommonR.string.please_accept_our) + " ")
        }

        pushStringAnnotation(
            tag = CommonR.string.terms_of_service.toString(),
            annotation = CommonR.string.terms_of_service.toString()
        )

        withStyle(
            style = SpanStyle(
                color = ColorTextSecondary,
                fontWeight = FontWeight.W700,
                fontSize = 16.ssp(),
                textDecoration = TextDecoration.Underline,
            )
        ) {
            append(stringResource(id = CommonR.string.terms_of_service))
        }
    }





    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .verticalScroll(rememberScrollState())
            .padding(top = 30.h(), start = 36.w(), end = 36.w()),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = (-20).w()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = ColorPrimaryDark,
                modifier = Modifier
                    .size(24.r())
                    .clickable {
                        viewModel.back()
                    },
            )
            Text(
                text = stringResource(id = CommonR.string.sign_up),
                style = subHeading1TextStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.w(), start = 24.w())
            )
        }
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
                .padding(top = 20.w(), bottom = 10.w())
                .align(Alignment.Start),
        )
        TextField(
            singleLine = true,
            value = state.email,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = {
                viewModel.onEvent(RegistrationEvent.OnEmailEnter(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .onFocusChanged {
                    viewModel.isEmailValid()
                }

                .conditional(state.isEmailValid) {
                    return@conditional border(
                        width = 1.dp, color = Color(0xFFE2E4EA), shape = RoundedCornerShape(15.dp)
                    )
                }
                .conditional(!state.isEmailValid) {
                    return@conditional border(
                        width = 1.dp, color = ColorError, shape = RoundedCornerShape(15.dp)
                    )
                },

            leadingIcon = {
                Image(
                    painter = painterResource(id = if (state.isEmailValid) DesignSystemR.drawable.ic_email else DesignSystemR.drawable.ic_red_error),
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


        if (!state.isEmailValid) {
            Text(
                text = context.getString(CommonR.string.please_enter_valid_email),
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

        Text(
            text = stringResource(id = CommonR.string.password),
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
                .padding(top = 20.h(), bottom = 10.h()),
        )
        TextField(
            singleLine = true,
            value = state.password,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {
                viewModel.onEvent(RegistrationEvent.OnPasswordEnter(it))
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .onFocusChanged {
                    viewModel.isPasswordValid()
                }
                .conditional(state.isPasswordValid) {
                    return@conditional border(
                        width = 1.dp, color = Color(0xFFE2E4EA), shape = RoundedCornerShape(15.dp)
                    )
                }
                .conditional(!state.isPasswordValid) {
                    return@conditional border(
                        width = 1.dp, color = ColorError, shape = RoundedCornerShape(15.dp)
                    )
                },
            leadingIcon = {
                Image(
                    painter = painterResource(id = if (state.isPasswordValid) DesignSystemR.drawable.ic_lock else DesignSystemR.drawable.ic_red_error),
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
                Icon(painter = painterResource(id = if (showPassword.value) DesignSystemR.drawable.ic_eye_slash else DesignSystemR.drawable.ic_eye_unslash),
                    contentDescription = "",
                    tint = ColorTextFieldPlaceholder,
                    modifier = Modifier.clickable {
                        showPassword.value = !showPassword.value
                    })
            },
            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        )
        if (!state.isPasswordValid) {
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
                    .padding(top = 4.h())
                    .align(Alignment.Start),
            )
        }

        Text(
            text = stringResource(id = CommonR.string.confirm_password),
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
                .padding(top = 20.h(), bottom = 10.h()),
        )
        TextField(
            singleLine = true,
            value = state.confirmPassword,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {
                viewModel.onEvent(RegistrationEvent.OnConfirmationPasswordEnter(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .onFocusChanged {
                    viewModel.isConfirmPasswordValid()
                }
                .conditional(state.isConfirmPasswordValid) {
                    return@conditional border(
                        width = 1.dp, color = Color(0xFFE2E4EA), shape = RoundedCornerShape(15.dp)
                    )
                }
                .conditional(!state.isConfirmPasswordValid) {
                    return@conditional border(
                        width = 1.dp, color = ColorError, shape = RoundedCornerShape(15.dp)
                    )
                },
            leadingIcon = {
                Image(
                    painter = painterResource(id = if (state.isConfirmPasswordValid) DesignSystemR.drawable.ic_lock else DesignSystemR.drawable.ic_red_error),
                    contentDescription = "",
                    modifier = Modifier.size(24.r())
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
                Icon(painter = painterResource(id = if (showConfirmPassword.value) DesignSystemR.drawable.ic_eye_slash else DesignSystemR.drawable.ic_eye_unslash),
                    contentDescription = "",
                    tint = ColorTextFieldPlaceholder,
                    modifier = Modifier.clickable {
                        showConfirmPassword.value = !showConfirmPassword.value
                    })
            },
            visualTransformation = if (showConfirmPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        )
        if (!state.isConfirmPasswordValid) {
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
                    .padding(top = 4.h())
                    .align(Alignment.Start),
            )
        }
        Spacer(modifier = Modifier.height(20.h()))

        Row {
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_calender),
                contentDescription = null,
                modifier = Modifier.size(24.r())
            )
            Spacer(modifier = Modifier.width(12.w()))
            Text(
                text = stringResource(id = CommonR.string.age_category),
                style = subHeadingFormTextStyle
            )
        }
        AgeGroupComposeSignup(
            AGE_LIST,
            state.ageCategory,
            modifier = Modifier,
            selectedAgeCategoryItem,
            onClick = {
                viewModel.onEvent(RegistrationEvent.OnAgeEnter(it))
            },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = (-16).w())
                .padding(top = 26.h())
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = state.isAgreedToTermsAndConditions,
                colors = CheckboxDefaults.colors(
                    checkmarkColor = ColorTextPrimary,
                    disabledCheckedColor = Color.LightGray,
                    checkedColor = Color.LightGray,
                    disabledIndeterminateColor = Color.LightGray,
                    disabledUncheckedColor = Color.LightGray,
                    uncheckedColor = if(!state.isTCValid) Color.Red else Color.LightGray,
                ), onCheckedChange = {
                    viewModel.onEvent(RegistrationEvent.OnAcceptTermsEnter(it))
                })

            ClickableText(text = termsConditions,
                style = bodyRegularTextStyle.copy(
                    color = ColorTextPrimary,
                    letterSpacing = .5.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(top = 25.h(), bottom = 25.h()),
                onClick = { offset ->
                    termsConditions.getStringAnnotations(
                        tag = CommonR.string.terms_of_service.toString(),
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        openExternalLink(TERMS_SERVICE_URL, context)
                    }
                })
        }
        if (!state.isTCValid) {
            Text(
                text = context.getString(CommonR.string.please_accept_our_terms),
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
                    .padding(bottom = 20.h())
                    .align(Alignment.Start),
                letterSpacing = .5.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        AppActionButtonCompose(stringId = CommonR.string.sign_up,
            bgColor = ColorPrimaryDark,
            textColor = Color.White,
            modifier = Modifier.padding(bottom = 55.h()),
            onActionButtonClick = {
                mail.value = state.email
                viewModel.onEvent(RegistrationEvent.OnSubmitClick)
            })

        if (state.showDialog)
            LoadingDialog {}

    }

}

@Composable
@Preview
fun PreviewSignUpScreen() {
    // SignUpScreen(onBack = { }, onSignUpSuccess = {})
}

