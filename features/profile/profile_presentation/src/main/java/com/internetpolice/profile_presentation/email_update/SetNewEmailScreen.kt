package com.internetpolice.profile_presentation.email_update

import android.widget.Toast
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.internetpolice.auth_presentation.isEmailValid
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetNewEmailScreen(
    onBack: () -> Unit,
    onNewEmailSubmit: (email: String, source: String) -> Unit,
    viewModel: SendEmailOTPViewModel = hiltViewModel()
) {
    var newEmailTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var confirmNewEmailTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    //Error state
    val newEmailErrorState = remember { mutableStateOf("") }
    val confirmNewEmailErrorState = remember { mutableStateOf("") }
    //Error state

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->

            when (event) {
                is UiEvent.Success -> {
                    onNewEmailSubmit(viewModel.state.email, Route.SET_NEW_EMAIL)
                }

                is UiEvent.ShowSnackbar -> {

                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {}
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
            modifier = Modifier
                .padding(top = 70.h(), start = 36.w(), end = 36.w()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = CommonR.string.change_email_address),
                style = heading1TextStyle,
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(id = CommonR.string.change_email_address_info),
                style = bodyRegularTextStyle.copy(color = ColorTextSecondary),
                modifier = Modifier.padding(top = 15.h()),
            )

            Text(
                text = stringResource(id = CommonR.string.new_email_address),
                style = subHeadingFormTextStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.h(), bottom = 10.h()),
            )
            TextField(
                value = newEmailTextFieldValue,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = {
                    newEmailTextFieldValue = it
                    viewModel.onEvent(SendEmailOTPEvent.OnEmailEnter(it.text))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .conditional(newEmailErrorState.value.isEmpty()) {
                        return@conditional border(
                            width = 1.dp,
                            color = Color(0xFFE2E4EA),
                            shape = RoundedCornerShape(15.dp)
                        )
                    }
                    .conditional(newEmailErrorState.value.isNotEmpty()) {
                        return@conditional border(
                            width = 1.dp,
                            color = ColorError,
                            shape = RoundedCornerShape(15.dp)
                        )
                    },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = if (newEmailErrorState.value.isEmpty()) DesignSystemR.drawable.ic_email else DesignSystemR.drawable.ic_red_error),
                        contentDescription = "",
                        modifier = Modifier.size(24.r())
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = CommonR.string.enter_your_new_email_address),
                        style = bodyRegularTextStyle.copy(color = ColorTextSecondary)
                    )
                },

                )
            if (newEmailErrorState.value.isNotEmpty()) {
                Text(
                    text = newEmailErrorState.value,
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
                text = stringResource(id = CommonR.string.confirm_new_email_address),
                style = subHeadingFormTextStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.h(), bottom = 10.h()),
            )
            TextField(
                value = confirmNewEmailTextFieldValue,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = {
                    confirmNewEmailTextFieldValue = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .conditional(confirmNewEmailErrorState.value.isEmpty()) {
                        return@conditional border(
                            width = 1.dp,
                            color = Color(0xFFE2E4EA),
                            shape = RoundedCornerShape(15.dp)
                        )
                    }
                    .conditional(confirmNewEmailErrorState.value.isNotEmpty()) {
                        return@conditional border(
                            width = 1.dp,
                            color = ColorError,
                            shape = RoundedCornerShape(15.dp)
                        )
                    },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = if (confirmNewEmailErrorState.value.isEmpty()) DesignSystemR.drawable.ic_email else DesignSystemR.drawable.ic_red_error),
                        contentDescription = "",
                        modifier = Modifier.size(24.r())
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = CommonR.string.enter_your_confirm_new_email_address),
                        style = bodyRegularTextStyle.copy(color = ColorTextSecondary)
                    )
                },

                )
            if (confirmNewEmailErrorState.value.isNotEmpty()) {
                Text(
                    text = confirmNewEmailErrorState.value,
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
                        .padding(top = 4.dp)
                        .align(Alignment.Start),
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AppActionButtonCompose(
            stringId = CommonR.string.verify,
            modifier = Modifier
                .padding(start = 36.w(), end = 36.w())
        ) {
            if (
                isEmailValid(newEmailTextFieldValue.text) &&
                isEmailValid(confirmNewEmailTextFieldValue.text) &&
                confirmNewEmailTextFieldValue.text == newEmailTextFieldValue.text
            ) {
                confirmNewEmailErrorState.value = ""
                newEmailErrorState.value = ""
                viewModel.onEvent(SendEmailOTPEvent.OnVerifyClick)

            } else {

                if (!isEmailValid(newEmailTextFieldValue.text)) {
                    newEmailErrorState.value =
                        context.getString(CommonR.string.invalid_email)
                } else {
                    newEmailErrorState.value = ""
                }

                if (!isEmailValid(confirmNewEmailTextFieldValue.text) ||
                    confirmNewEmailTextFieldValue.text != newEmailTextFieldValue.text
                ) {
                    confirmNewEmailErrorState.value =
                        context.getString(CommonR.string.invalid_email)
                } else {
                    confirmNewEmailErrorState.value = ""
                }

            }
        }
        Spacer(modifier = Modifier.height(54.h()))
    }
}

@Composable
@DevicePreviews
fun PreviewSetNewEmailScreen() {
    /*  SetNewEmailScreen(onBack = {

      }) {

      }*/
}
