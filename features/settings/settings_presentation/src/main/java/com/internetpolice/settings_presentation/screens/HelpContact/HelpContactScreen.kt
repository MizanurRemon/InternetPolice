package com.internetpolice.settings_presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.internetpolice.core.common.R
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.settings_presentation.helpers.ScreenSize
import com.internetpolice.settings_presentation.screens.HelpContact.HelpContactViewModel
import java.util.*
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.common.util.openMailBox
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.settings_presentation.screens.HelpContact.HelpContactEvent
import com.internetpolice.settings_presentation.screens.report_problem.ReportProblemEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpContactScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    onFaqClick: () -> Unit,
    onTechnicalIssueFormClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: HelpContactViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isDescriptionBlank by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->

            when (event) {
                is UiEvent.Success -> {
                    onSuccess()
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


    val contentString = buildAnnotatedString {
        append(stringResource(CommonR.string.help_and_contact_text) + " ")

        pushStringAnnotation(
            tag = CommonR.string.faq.toString(),
            annotation = CommonR.string.faq.toString()
        )

        withStyle(style = SpanStyle(ColorPrimaryDark, fontWeight = FontWeight.Bold)) {
            append(stringResource(id = CommonR.string.faq).replace(".", ""))
        }

        append(".")
    }

    val noteText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(stringResource(id = CommonR.string.note) + ": ")
        }

        append(stringResource(id = CommonR.string.for_technical_related_issues) + " ")

        pushStringAnnotation(
            tag = CommonR.string.technical_issue_form.toString(),
            annotation = CommonR.string.technical_issue_form.toString()
        )

        withStyle(style = SpanStyle(ColorPrimaryDark, fontWeight = FontWeight.Bold)) {
            append(stringResource(id = CommonR.string.technical_issue_form).replace(".", ""))
        }

        append(".")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrushReversed),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AppToolbarCompose(
            title = stringResource(id = R.string.back),
            modifier =
            Modifier
                .padding(start = 16.w(), top = 24.h(), end = 16.w())
                .background(Color.Transparent)
        ) {
            onBack()
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_police_help),
                modifier = Modifier
                    .height(300.h())
                    .fillMaxWidth(),
                contentDescription = ""
            )

            Box(modifier = Modifier.padding(top = 125.h())) {
                Image(
                    painter = painterResource(id = DesignSystemR.drawable.ic_help_contact_curve_bg),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 120.h(), start = 32.w(), end = 32.w(), bottom = 55.h())
                ) {
                    Text(
                        text = stringResource(id = CommonR.string.contact_our_team),
                        style = subHeading1TextStyle,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )

                    ClickableText(text = contentString,
                        style = bodyRegularTextStyle.copy(
                            color = ColorTextPrimary,
                            letterSpacing = .5.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(top = 25.h(), bottom = 25.h()),
                        onClick = { offset ->
                            contentString.getStringAnnotations(
                                tag = CommonR.string.faq.toString(),
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let {
                                onFaqClick()
                            }
                        })

                    OutlinedTextField(
                        shape = RoundedCornerShape(15.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = if (isDescriptionBlank) Color.Red else Color.White,
                            unfocusedBorderColor = if (isDescriptionBlank) Color.Red else Color.White,
                            cursorColor = Color.Black,
                            textColor = Color.Black,
                        ),
                        value = viewModel.state.description,
                        onValueChange = {
                            viewModel.onEvent(HelpContactEvent.OnDescriptionEnter(it))
                            isDescriptionBlank = it.isEmpty()
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = CommonR.string.describe),
                                style = bodyRegularTextStyle.copy(color = ColorTextFieldPlaceholder)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .shadow(elevation = 1.dp, shape = RoundedCornerShape(15.dp))
                    )

                    ClickableText(
                        modifier = Modifier.padding(top = 25.h()),
                        text = noteText,
                        style = bodyRegularTextStyle.copy(
                            color = ColorTextPrimary,
                            letterSpacing = .5.sp,
                            textAlign = TextAlign.Center
                        ),
                        onClick = { offset ->
                            noteText.getStringAnnotations(
                                tag = CommonR.string.technical_issue_form.toString(),
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let {
                                onTechnicalIssueFormClick()
                            }
                        }
                    )


                    Spacer(modifier = Modifier.weight(1f))

                    AppActionButtonCompose(stringId = CommonR.string.send_message) {
                        if (viewModel.state.description.isEmpty()) {
                            isDescriptionBlank = true
                        } else {
                            viewModel.onEvent(HelpContactEvent.OnSubmitClick)
                        }
                    }
                }
            }
        }
    }


    if (viewModel.state.isShowDialog)
        LoadingDialog {}
}

@Composable
@DevicePreviews
fun PreviewHelpContactScreen() {
    val snackBarHostState = remember { SnackbarHostState() }
    HelpContactScreen(
        onBack = {},
        onSuccess = {},
        onFaqClick = {},
        onTechnicalIssueFormClick = {},
        snackbarHostState = snackBarHostState
    )
}