package com.internetpolice.auth_presentation.screens

import android.annotation.SuppressLint
import android.view.Gravity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.internetpolice.core.common.R
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.ColorError
import com.internetpolice.core.designsystem.theme.ColorPrimaryDark
import com.internetpolice.core.designsystem.theme.ColorTextFieldPlaceholder
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.conditional
import com.internetpolice.core.designsystem.theme.fontRoboto
import com.internetpolice.core.designsystem.theme.parseBold
import com.internetpolice.core.designsystem.theme.subHeading1TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.common.R as CommonR

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpNotAvailableScreen(
    snackBarHostState: SnackbarHostState,
    signUpNotAvailableViewModel: SignUpNotAvailableViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val state = signUpNotAvailableViewModel.state
    val context = LocalContext.current
    var dialogOpen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        signUpNotAvailableViewModel.uiEvent.collect() { event ->

            when (event) {
                is UiEvent.Success -> {
                    dialogOpen = state.isSignUpNotAvailableDialogOpen
                    onSuccess()
                }

                is UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short,
                    )
                }

                else -> {}
            }

        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = AppBrush)
                .padding(
                    top = 55.h(), bottom = 55.h(), start = 30.w(), end = 30.w()
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_police_with_board_female),
                contentDescription = "",
                modifier = Modifier.height((configuration.screenHeightDp / 4).dp)
            )
            Spacer(modifier = Modifier.height(20.h()))

            Text(
                text = stringResource(
                    id = R.string.sign_up_not_available
                ), style = subHeading1TextStyle
            )

            Spacer(modifier = Modifier.height(20.h()))

            Text(
                text = stringResource(id = R.string.sign_up_not_available_text1).parseBold(),
                style = bodyRegularTextStyle,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(20.h()))

            Text(
                text = stringResource(
                    id = R.string.sign_up_not_available_text2
                ).parseBold(),
                style = bodyRegularTextStyle,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(20.h()))

            TextField(
                value = state.email,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = {
                    signUpNotAvailableViewModel.onEvent(SignUpNotAvailableEvent.OnEmailEnter(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .onFocusChanged {
                        signUpNotAvailableViewModel.isEmailValid()
                    }

                    .conditional(state.isEmailValid) {
                        return@conditional border(
                            width = 1.dp,
                            color = Color(0xFFE2E4EA),
                            shape = RoundedCornerShape(15.dp)
                        )
                    }
                    .conditional(!state.isEmailValid) {
                        return@conditional border(
                            width = 1.dp, color = ColorError, shape = RoundedCornerShape(15.dp)
                        )
                    },

                leadingIcon = {
                    if (state.isEmailValid) {
                        Image(
                            painter = painterResource(id = DesignSystemR.drawable.ic_email),
                            contentDescription = "",
                            modifier = Modifier.size(24.r())
                        )
                    }
                },

                trailingIcon = {
                    if (!state.isEmailValid) {
                        Image(
                            painter = painterResource(id = DesignSystemR.drawable.ic_red_error),
                            contentDescription = "",
                            modifier = Modifier.size(24.r())
                        )
                    }
                },

                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_your_email), style = TextStyle(
                            color = ColorTextFieldPlaceholder,
                        )
                    )
                }

            )


            if (!state.isEmailValid) {
                Text(
                    text = context.getString(R.string.please_enter_email_address),
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

            AppActionButtonCompose(
                stringId = R.string.continues, modifier = Modifier
            ) {
                signUpNotAvailableViewModel.onEvent(SignUpNotAvailableEvent.OnContinueClick)
                dialogOpen = signUpNotAvailableViewModel.state.isSignUpNotAvailableDialogOpen
            }

            if (dialogOpen) {
                ConsentDialog(
                    signUpNotAvailableViewModel,
                    dialogOpen = dialogOpen,
                    onDismissRequest = {
                        dialogOpen = false
                    }
                ) {
                    onSuccess()
                }
            }

        }

        Row(
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .padding(top = 20.h(), end = 40.w())
        ) {
            Image(painter = painterResource(id = DesignSystemR.drawable.ic_blue_cross),
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onBack()
                    })
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConsentDialog(
    signUpNotAvailableViewModel: SignUpNotAvailableViewModel,
    dialogOpen: Boolean,
    onDismissRequest: (isAllowed: Boolean) -> Unit,
    onSuccess: () -> Unit
) {
    if (dialogOpen) {
        Dialog(properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ), onDismissRequest = {
            onDismissRequest(true)
        }) {
            val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
            val configuration = LocalConfiguration.current
            dialogWindowProvider.window.setGravity(Gravity.CENTER)
            Surface(
                color = Color.White, shape = RoundedCornerShape(30.dp), modifier = Modifier
                    .padding(
                        start = (configuration.screenWidthDp / 8).dp,
                        end = (configuration.screenWidthDp / 8).dp
                    )
                    .fillMaxWidth()

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.w())
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.are_you_sure
                        ), style = subHeading1TextStyle
                    )


                    Spacer(modifier = Modifier.height(20.h()))

                    Text(
                        text = stringResource(
                            id = R.string.sign_up_not_available_dialog_text
                        ),
                        style = bodyRegularTextStyle,
                        fontSize = 16.sp,
                    )

                    Spacer(modifier = Modifier.height(20.h()))

                    Image(
                        painter = painterResource(id = DesignSystemR.drawable.ic_cop_notification_disable),
                        contentDescription = "",
                        modifier = Modifier
                            .scale(1f)
                            .height(200.h())
                    )

                    Spacer(modifier = Modifier.height(20.h()))

                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(ColorPrimaryDark), onClick = {
                                onDismissRequest(true)
                            }, modifier = Modifier
                                .weight(1f)
                                .height(52.h())
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = CommonR.string.no),
                                style = TextStyle(
                                    color = Color.White,
                                    fontFamily = fontRoboto,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    letterSpacing = .5.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(12.w()))
                        Button(
                            shape = RoundedCornerShape(30.dp),
                            border = BorderStroke(1.dp, Color.Red),
                            colors = ButtonDefaults.buttonColors(Color.White),
                            onClick = {
                                onDismissRequest(false)
//                                onSuccess()
                                signUpNotAvailableViewModel.onEvent(SignUpNotAvailableEvent.OnYesClick)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.h())
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = CommonR.string.yes),
                                style = TextStyle(
                                    color = Color.Red,
                                    fontFamily = fontRoboto,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    letterSpacing = .5.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewConsentDialog() {
//    ConsentDialog(
//        signUpNotAvailableViewModel = signUpNotAvailableViewModel,
//        dialogOpen = true,
//        onDismissRequest = {}) {}
}

@DevicePreviews
@Composable
fun PreviewSignUpNotAvailableScreen() {
    //  SignUpNotAvailableScreen(onBack = {}, onSuccess = {})
}