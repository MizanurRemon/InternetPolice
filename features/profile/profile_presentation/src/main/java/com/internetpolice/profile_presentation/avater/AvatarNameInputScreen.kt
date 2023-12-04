package com.internetpolice.profile_presentation.avater

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarNameInputScreen(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onGoToAvatarCreation: (String) -> Unit,
    viewModel: AvatarViewModel = hiltViewModel(),
) {
    val isNameInputDone = remember {
        mutableStateOf(false)
    }
    val showEditButton = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->
            when (event) {
                is UiEvent.Success -> {
                    isNameInputDone.value = true
                    GlobalScope.launch {
                        delay(1000L)
                        showEditButton.value = true
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


    val infiniteTransition = rememberInfiniteTransition()
    val animatedSize by infiniteTransition.animateFloat(
        initialValue = 55f,
        targetValue = 65f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun InputCompose() {
        Column(modifier = Modifier.padding(start = 16.w(), end = 16.w(), top = 50.h())) {
            Text(
                text = stringResource(id = CommonR.string.please_enter_your_username),
                style = subHeadingFormTextStyle
            )
            TextField(
                value = viewModel.state.name,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                onValueChange = {
                    viewModel.onEvent(AvatarEvent.OnNameChange(it))
                },
                modifier = Modifier
                    .padding(top = 10.h())
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
                    Icon(
                        painter = painterResource(id = DesignSystemR.drawable.ic_profile),
                        contentDescription = "",
                        modifier = Modifier.size(24.r()),
                        tint = ColorTextFieldPlaceholder
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = CommonR.string.user_name), style = TextStyle(
                            color = ColorTextFieldPlaceholder,
                        )
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
            Spacer(modifier = Modifier.weight(1f))
            AppActionButtonCompose(
                stringId = CommonR.string.continues,
                modifier = Modifier.padding(bottom = 55.h())
            ) {
                viewModel.onEvent(AvatarEvent.OnSubmit)
            }
            if (viewModel.state.isShowLoading)
                LoadingDialog {}
        }
    }

    return Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .background(color = Color.Black.copy(alpha = if (showEditButton.value) 0.5f else 0f))
    ) {
        Column(
            modifier = Modifier
                .padding(top = 30.dp, start = 20.w(), end = 20.w()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppToolbarCompose(title = stringResource(id = CommonR.string.home)) {
                onBack()
            }
            Text(
                text = stringResource(
                    id = if (isNameInputDone.value) CommonR.string.welcome
                    else CommonR.string.name_your_avatar
                ),
                modifier = Modifier
                    .padding(top = 60.h(), bottom = 50.h())
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = heading1TextStyle.copy(
                    color = if (isNameInputDone.value)
                        ColorPrimaryDark
                    else ColorTextPrimary
                )
            )

            ImageWithEditButton(showEditButton, animatedSize, editClick = {
                onGoToAvatarCreation(viewModel.state.name)
            })


            if (!isNameInputDone.value)
                InputCompose()
            if (isNameInputDone.value) {
                NameShowingCompose(viewModel.state.name)
            }

        }
    }
}

@Composable
fun ImageWithEditButton(
    showEditButton: MutableState<Boolean>,
    animatedSize: Float,
    editClick: () -> Unit
) {
    Box(modifier = Modifier) {
        Image(
            painter = painterResource(id = DesignSystemR.drawable.ic_default_avatar),
            contentDescription = null,
            modifier = Modifier.size(280.h())
        )
        if (showEditButton.value)
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_edit),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 25.r())
                    .size(animatedSize.r())
                    .align(alignment = Alignment.BottomEnd)
                    .clickable { editClick() }
            )
    }
}

@Composable
fun NameShowingCompose(name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 30.dp)
    ) {
        Text(text = name, style = heading2TextStyle)
        Spacer(modifier = Modifier.height(20.h()))
        Text(
            text = stringResource(id = CommonR.string.internet_police_officer),
            style = bodyRegularTextStyle
        )
    }
}

/*
@Composable
@DevicePreviews
fun PreviewAvatarNameInputScreen() {
}
*/


@Composable
@Preview
fun PreviewImageWithEditButton() {
    val showEditButton = remember {
        mutableStateOf(false)
    }
    val infiniteTransition = rememberInfiniteTransition()
    val animatedSize by infiniteTransition.animateFloat(
        initialValue = 55f,
        targetValue = 65f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    ImageWithEditButton(
        showEditButton,
        animatedSize,
        editClick = {}
    )
}