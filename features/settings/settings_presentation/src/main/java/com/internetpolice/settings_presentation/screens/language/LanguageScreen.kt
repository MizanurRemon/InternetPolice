package com.internetpolice.settings_presentation.screens.language

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.updateSharedPreferences
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.languageList
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.settings_presentation.helpers.ScreenSize
import java.util.*
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LanguageScreen(
    navController: NavController,
    isInitial: Boolean = false,
    viewModel: LanguageViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->

            when (event) {
                is UiEvent.Success -> {
                    if (isInitial) navController.navigate(Route.ALWAYS_PROTECTED)
                    else navController.navigateUp()
                }

                is UiEvent.ShowSnackbar -> {
                }

                is UiEvent.NavigateUp -> {
                    navController.navigateUp()
                }

                else -> {}
            }

        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                AppActionButtonCompose(
                    stringId = if (isInitial) CommonR.string.continues
                    else CommonR.string.choose_language, modifier = Modifier.padding(
                        start = 36.w(), end = 36.w(), bottom = 55.h()
                    )
                ) {
                    updateSharedPreferences(viewModel.state.tag, context)
                    viewModel.onNext()
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.w(), top = 24.h(), end = 16.w(), bottom = 70.h())
            ) {
                if (!isInitial) AppToolbarCompose(title = stringResource(id = CommonR.string.back)) {
                    viewModel.onBack()
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.h(), bottom = 24.h()),
                    text = stringResource(
                        id = if (isInitial) CommonR.string.welcome
                        else CommonR.string.choose_language
                    ),
                    textAlign = TextAlign.Center,
                    style = if (isInitial) heading2TextStyle
                    else subHeading1TextStyle
                )
                if (isInitial) Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.h()),
                    text = stringResource(
                        id = CommonR.string.please_select_your_preferred_lang
                    ),
                    textAlign = TextAlign.Center,
                    style = bodyRegularTextStyle.copy(color = ColorTextSecondary)
                )
                LazyColumn(
                    modifier = Modifier.height(
                        (ScreenSize.height() * 0.6).dp
                    )
                ) {
                    items(languageList) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.h(), bottom = 6.h())
                                .clickable {
                                    viewModel.updateState(item)
                                },
                            colors = CardDefaults.cardColors(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = if (item.title.name == viewModel.state.title.name) colorResource(
                                            id = DesignSystemR.color.slected_language_bg_color
                                        ) else {
                                            colorResource(id = DesignSystemR.color.white)
                                        }
                                    )
                                    .border(
                                        shape = RoundedCornerShape(16.dp),
                                        border = if (item.title.name == viewModel.state.title.name) BorderStroke(
                                            2.dp, colorResource(
                                                id = DesignSystemR.color.default_icon_color
                                            )
                                        ) else BorderStroke(
                                            2.dp, colorResource(
                                                id = DesignSystemR.color.white
                                            )
                                        )
                                    )
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {

                                Image(
                                    painter = painterResource(item.image),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center
                                )

                                Text(
                                    modifier = Modifier.padding(start = 10.w()),
                                    text = item.title.name,
                                    style = subHeading1TextStyle,
                                    fontWeight = FontWeight.W700
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                if (item.title.name == viewModel.state.title.name) {
                                    Icon(
                                        painter = painterResource(id = DesignSystemR.drawable.ic_baseline_check_24),
                                        tint = colorResource(id = DesignSystemR.color.default_icon_color),
                                        modifier = Modifier.size(16.dp),
                                        contentDescription = ""
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    if (viewModel.isShowDialog.value)
        LoadingDialog {}
}

@Composable
@DevicePreviews
fun PreviewSettingsLanguageScreen() {
    //LanguageScreen(isInitial = true)
}

@Composable
@DevicePreviews
fun PreviewInitialLanguageScreen() {
    // LanguageScreen(isInitial = true)
}