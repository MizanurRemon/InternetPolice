package com.internetpolice.report_presentation.report

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.report_presentation.ReportEvent
import com.internetpolice.report_presentation.ReportViewModel
import com.internetpolice.core.common.R as CommonR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDescriptionInputScreen(
    viewModel: ReportViewModel,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->
            when (event) {
                is UiEvent.Success -> {
                    navController.navigate(Route.REPORT_RECEIVE_DONE + "/${viewModel.state.selectedDomain}")
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
            .padding(top = 30.h(), start = 16.w(), end = 16.w()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppToolbarCompose(title = stringResource(id = CommonR.string.home)) {
            viewModel.onEvent(ReportEvent.OnBack)
        }
        Spacer(modifier = Modifier.height(100.h()))
        Text(
            text = stringResource(id = CommonR.string.describe_your_experience),
            style = heading1TextStyle.copy(color = ColorTextPrimary)
        )
        Spacer(modifier = Modifier.height(40.h()))
        Text(
            text = stringResource(id = CommonR.string.describe_your_experience_info),
            style = bodyRegularTextStyle.copy(color = ColorTextSecondary),
            modifier = Modifier.padding(start = 20.w(), end = 20.w(), bottom = 24.h())
        )
        OutlinedTextField(
            value = viewModel.state.description ?: "",
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = {
                viewModel.onEvent(ReportEvent.OnDescriptionInput(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(240.r())
                .padding(horizontal = 20.r())
                .clip(RoundedCornerShape(15.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFFE2E4EA),
                    shape = RoundedCornerShape(15.dp)
                ),
            placeholder = {
                Text(
                    text = stringResource(id = CommonR.string.describe_your_experience_placeholder),
                    style = TextStyle(
                        color = ColorTextFieldPlaceholder,
                    )
                )
            },
        )
        Spacer(modifier = Modifier.weight(1f))
        AppActionButtonCompose(stringId = CommonR.string.report) {
            viewModel.onEvent(ReportEvent.OnSubmitReport)
        }
        Spacer(modifier = Modifier.height(55.h()))

        if (viewModel.state.isShowLoading)
            LoadingDialog {}
    }
}


@Composable
@DevicePreviews
fun PreviewReportDescriptionInputScreen() {
//    ReportDescriptionInputScreen(onBack = {
//    }, onDescriptionInputted = {
//
//    })
}
