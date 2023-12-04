package com.internetpolice.profile_presentation.acc_settings

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AgeGroupCompose
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


@Composable
fun ManageAccountScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onChangePassword: () -> Unit,
    onChangeEmail: () -> Unit,
    onCustomizeAvatar: () -> Unit,
    viewModel: AccSettingsViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val openDeleteAccountDialog = remember {
        mutableStateOf(false)
    }
    ShowPopup(
        openDialog = openDeleteAccountDialog,
        titleResId = CommonR.string.delete_account,
        descriptionResId = CommonR.string.delete_my_account_info,
        dismissTextResId = CommonR.string.cancel,
        confirmTextResId = CommonR.string.delete_account,
        important = true
    ) {
        viewModel.onEvent(AccSettingsEvent.OnDeleteAccount)
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->
            when (event) {
                is UiEvent.Success -> {
                    onBack()
                }

                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short,
                    )
                }

                is UiEvent.NavigateUp -> {
                    navController.navigate(Route.SIGN_IN) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }

        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
    ) {
        Column(
            modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppToolbarCompose(
                title = stringResource(id = CommonR.string.back),
                modifier = Modifier.padding(start = 20.w(), top = 30.h(), bottom = 50.h())
            ) {
                onBack()
            }
            Text(stringResource(id = CommonR.string.manage_account), style = heading1TextStyle)
            EmailCompose(onChangeEmail, viewModel.state.email)
            InfoCompose(onCustomizeAvatar, viewModel)
            PasswordCompose(onChangePassword)
            DeleteMyAccountCompose {
                openDeleteAccountDialog.value = true
            }
            Spacer(modifier = Modifier.weight(1f))
            AppActionButtonCompose(
                stringId = CommonR.string.save_changes,
                modifier = Modifier.padding(start = 36.w(), end = 36.w(), bottom = 55.h())
            ) {
                viewModel.onEvent(AccSettingsEvent.OnSubmit)
            }
            if (viewModel.state.isShowLoading)
                LoadingDialog {}
        }
    }
}

@Composable
private fun EmailCompose(onChangeEmail: () -> Unit, email: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.h())
            .background(color = Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 32.w(), vertical = 20.h()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = email, style = bodyRegularTextStyle.copy(color = ColorTextPrimary)
            )
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_green_check),
                contentDescription = null,
                modifier = Modifier.padding(start = 4.w())

            )
            Text(text = stringResource(id = CommonR.string.change),
                style = bodyRegularTextStyle.copy(color = Color(0xff777A95))
                    .copy(textAlign = TextAlign.Right),
                modifier = Modifier
                    .weight(1f)
                    .noRippleClickable { onChangeEmail() })
            Image(painter = painterResource(id = DesignSystemR.drawable.ic_chevron_right),
                contentDescription = null,
                modifier = Modifier.noRippleClickable { onChangeEmail() })
        }
    }
}

@Composable
private fun CreateCustomizeAvatarCompose(onCustomizeAvatar: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 24.h())
            .fillMaxSize()
            .background(color = Color.White)
            .noRippleClickable {
                onCustomizeAvatar()
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.w(), vertical = 22.h()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = DesignSystemR.drawable.ic_profile),
                contentDescription = null,
                tint = Color(0xffA0A2B3)
            )
            Text(
                text = stringResource(id = CommonR.string.create_customized_avatar),
                style = bodyMediumTextStyle.copy(textAlign = TextAlign.Left),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 18.w())
            )
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_chevron_right),
                contentDescription = null,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun PasswordCompose(onChangePassword: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.h())
            .background(color = Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 32.h(), vertical = 20.w()),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_lock),
                contentDescription = null,
                modifier = Modifier.size(24.r())
            )
            Spacer(modifier = Modifier.width(12.w()))
            Text(
                text = stringResource(id = CommonR.string.password),
                style = bodyMediumTextStyle.copy(color = Color(0xff3E6FCC))
            )
            Text(text = stringResource(id = CommonR.string.change),
                style = bodyRegularTextStyle.copy(color = Color(0xff777A95))
                    .copy(textAlign = TextAlign.Right),
                modifier = Modifier
                    .noRippleClickable { onChangePassword() }
                    .weight(1f))
            Image(painter = painterResource(id = DesignSystemR.drawable.ic_chevron_right),
                contentDescription = null,
                modifier = Modifier.noRippleClickable { onChangePassword() })
        }
    }
}


@Composable
private fun DeleteMyAccountCompose(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.h(), bottom = 42.h())
            .background(color = Color.White)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 32.w(), vertical = 20.h()),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_account_delete),
                contentDescription = null,
                modifier = Modifier.size(24.r())
            )
            Spacer(modifier = Modifier.width(12.w()))
            Text(
                text = stringResource(id = CommonR.string.delete_my_account),
                style = bodyBoldTextStyle.copy(color = ColorTextPrimary)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InfoCompose(onCustomizeAvatar: () -> Unit, viewModel: AccSettingsViewModel) {

    Card(
        modifier = Modifier
            .padding(top = 16.h())
            .background(color = Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 34.w(), vertical = 22.h()),
        ) {

            Text(
                text = stringResource(id = CommonR.string.user_name),
                style = bodyMediumTextStyle.copy(color = Color(0xff3E6FCC)),
                modifier = Modifier.padding(bottom = 16.h())
            )

            // CreateCustomizeAvatarCompose(onCustomizeAvatar = onCustomizeAvatar)

            val corner = 8.r()
            TextField(
                value = viewModel.state.name,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = {
                    viewModel.onEvent(AccSettingsEvent.OnNameChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(corner))
                    .conditional(viewModel.state.name.isNotEmpty()) {
                        return@conditional border(
                            width = 1.4.dp,
                            color = Color(0xFFE2E4EA),
                            shape = RoundedCornerShape(corner)
                        )
                    }
                    .conditional(viewModel.state.name.isEmpty()) {
                        return@conditional border(
                            width = 1.4.dp, color = ColorError, shape = RoundedCornerShape(corner)
                        )
                    },
            )
            if (viewModel.state.isError) {
                Text(
                    text = stringResource(id = CommonR.string.invalid_name),
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
            Row(modifier = Modifier.padding(top = 16.h())) {
                Image(
                    painter = painterResource(id = DesignSystemR.drawable.ic_calender),
                    contentDescription = null,
                    modifier = Modifier.size(24.r())
                )
                Spacer(modifier = Modifier.width(12.w()))
                Text(
                    text = stringResource(id = CommonR.string.age_category),
                    style = subHeadingFormTextStyle.copy(color = Color(0xff3E6FCC))
                )
            }
            AgeGroupCompose(
                listOf("-18", "18-34", "35-60", "60+"),
                viewModel.state.ageCategory,
                modifier = Modifier,
                size = 75.r(),
            ) {
                viewModel.onEvent(AccSettingsEvent.OnAgeCategoryChange(it))
            }
        }

    }
}

@DevicePreviews
@Composable
fun PreviewManageAccountScreen() {
//    ManageAccountScreen(onChangeEmail = {},
//        onChangePassword = {
//
//        }, onCustomizeAvatar = {
//        }) {
//
//    }
}