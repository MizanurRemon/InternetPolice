package com.internetpolice.auth_presentation.verify

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import kotlinx.coroutines.delay
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailOTPScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: VerifyEmailOtpViewModel = hiltViewModel(),
    onSubmit: (email: String) -> Unit,
    email: String,
    source: String
) {

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val state = viewModel.state

    val remaining = remember { mutableStateOf(5 * 60) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val sendAgainString = buildAnnotatedString {

        withStyle(style = SpanStyle(color = LIGHT_BLACK, fontWeight = FontWeight.Medium)) {
            append(stringResource(CommonR.string.donot_get_any_code) + " ")
        }

        pushStringAnnotation(
            tag = CommonR.string.send_again.toString(),
            annotation = CommonR.string.send_again.toString()
        )

        withStyle(style = SpanStyle(ColorPrimaryDark, fontWeight = FontWeight.Bold)) {
            append(stringResource(id = CommonR.string.send_again).replace(".", ""))
        }

    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->

            when (event) {
                is UiEvent.Success -> {

                    if (source == Route.SIGN_IN) {
                        navController.navigate(Route.NOTIFICATION_ON_OFF + "/${true}") {
                            popUpTo(navController.graph.id)
                        }
                    } else {
                        onSubmit(state.newEmail)
                    }
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
    LaunchedEffect(key1 = Unit, block = {
        delay(1000)
        focusRequester.requestFocus()
    })
    LaunchedEffect(
        key1 = state.digit1,
    ) {
        if (state.digit1.isNotEmpty()) {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Next,
            )
        }
    }
    LaunchedEffect(
        key1 = state.digit2,
    ) {
        if (state.digit2.isNotEmpty()) {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Next,
            )
        } else {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Previous,
            )
        }
    }

    LaunchedEffect(
        key1 = state.digit3,
    ) {
        if (state.digit3.isNotEmpty()) {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Next,
            )
        } else {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Previous,
            )
        }
    }

    LaunchedEffect(
        key1 = state.digit4,
    ) {
        if (state.digit4.isNotEmpty()) {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Next,
            )
        } else {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Previous,
            )
        }
    }
    LaunchedEffect(
        key1 = state.digit5,
    ) {
        if (state.digit5.isEmpty()) {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Previous,
            )
        }
    }
    LaunchedEffect(key1 = remaining) {
        while (remaining.value > 0) {
            delay(1000)
            remaining.value--
        }
    }

    if (state.restartTimer) {
        remaining.value = 5 * 60
        viewModel.timerReset()
    }

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .padding(top = 40.dp, start = 35.dp, end = 35.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = CommonR.string.verify_email_address),
            style = heading2TextStyle,
        )
        Text(
            text = stringResource(id = CommonR.string.verify_email_address_info),
            style = TextStyle(
                fontFamily = fontRoboto,
                fontSize = 16.sp,
                color = ColorTextSecondary,
                fontWeight = FontWeight.W300,
                lineHeight = 25.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 15.dp),
        )
        Image(
            painter = painterResource(id = DesignSystemR.drawable.ic_verify_email),
            contentDescription = null,
            modifier = Modifier.padding(top = 15.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.digit1,
                onValueChange = {
                    if (it.length > 1) return@OutlinedTextField
                    viewModel.onEvent(VerifyEmailOtpEvent.OnDigit1Enter(it))

                },
                isError = state.isError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                shape = RoundedCornerShape(7.dp),
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(color = Color.White)
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ColorTextPrimary,
                    unfocusedBorderColor = Color(0xffD8E0FF),
                ),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )
            OutlinedTextField(
                value = state.digit2,
                onValueChange = {
                    if (it.length > 1) return@OutlinedTextField
                    viewModel.onEvent(VerifyEmailOtpEvent.OnDigit2Enter(it))

                },
                isError = state.isError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                shape = RoundedCornerShape(7.dp),
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xffD8E0FF),
                    focusedBorderColor = ColorTextPrimary,
                ),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )
            OutlinedTextField(
                value = state.digit3,
                onValueChange = {
                    if (it.length > 1) return@OutlinedTextField
                    viewModel.onEvent(VerifyEmailOtpEvent.OnDigit3Enter(it))

                },
                isError = state.isError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                shape = RoundedCornerShape(7.dp),
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xffD8E0FF),
                    focusedBorderColor = ColorTextPrimary
                ),
                textStyle = TextStyle(textAlign = TextAlign.Center)


            )
            OutlinedTextField(
                value = state.digit4,
                onValueChange = {
                    if (it.length > 1) return@OutlinedTextField
                    viewModel.onEvent(VerifyEmailOtpEvent.OnDigit4Enter(it))

                },
                isError = state.isError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                shape = RoundedCornerShape(7.dp),
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xffD8E0FF),
                    focusedBorderColor = ColorTextPrimary
                ),
                textStyle = TextStyle(textAlign = TextAlign.Center)

            )
            OutlinedTextField(
                value = state.digit5,
                onValueChange = {
                    if (it.length > 1) return@OutlinedTextField
                    viewModel.onEvent(VerifyEmailOtpEvent.OnDigit5Enter(it))

                },
                isError = state.isError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                shape = RoundedCornerShape(7.dp),
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xffD8E0FF),
                    focusedBorderColor = ColorTextPrimary
                ),
                textStyle = TextStyle(textAlign = TextAlign.Center)

            )
        }
        if (state.isError) {
            Text(
                text = context.getString(CommonR.string.incorrect_code),
                style = TextStyle(
                    fontWeight = FontWeight.W400,
                    fontFamily = fontRoboto,
                    lineHeight = 24.sp,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Left,
                    color = ColorError
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .align(Alignment.Start),
            )
        }
        Row(
            modifier = Modifier.padding(top = 20.dp),
        ) {
            Text(
                text = stringResource(id = CommonR.string.expired_in),
                color = Color(0xff818181),
                fontSize = 14.sp,
                fontWeight = FontWeight.W400
            )
            Spacer(modifier = Modifier.width(5.w()))
            Text(
                text = secondsToRemainingTime(remaining.value),
                color = Color(0xff818181),
                fontSize = 14.sp,
                fontWeight = FontWeight.W500
            )
        }
        Row(
            modifier = Modifier.padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ClickableText(
                text = sendAgainString,
                style = bodyRegularTextStyle.copy(
                    color = ColorTextPrimary,
                    letterSpacing = .5.sp,
                    textAlign = TextAlign.Center
                ),
                onClick = { offset ->
                    sendAgainString.getStringAnnotations(
                        tag = CommonR.string.send_again.toString(),
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        viewModel.onEvent(VerifyEmailOtpEvent.OnResendClick)
                    }
                }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        AppActionButtonCompose(stringId = CommonR.string.verify,
            bgColor = ColorPrimaryDark,
            textColor = Color.White,
            modifier = Modifier.padding(bottom = 55.dp),
            onActionButtonClick = { viewModel.onEvent(VerifyEmailOtpEvent.OnSubmitClick) })
        if (state.isShowDialog)
            LoadingDialog {}
    }

}

@Composable
@Preview
fun PreviewVerifyEmailScreen() {
    // VerifyEmailOTPScreen()
}

fun secondsToRemainingTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}


