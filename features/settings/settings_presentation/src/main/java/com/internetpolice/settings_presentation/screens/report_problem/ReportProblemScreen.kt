package com.internetpolice.settings_presentation.screens.report_problem

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.internetpolice.core.common.R
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.REPORT_SUBJECT_LIST
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportProblemScreen(
    onBack: () -> Unit,
    navController: NavController,
    snackBarHostState: SnackbarHostState,
    viewModel: ReportProblemViewModel = hiltViewModel(),
    onGeneralClick: () -> Unit
) {

    var selectSubjectWarning by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    var isDescriptionBlank by remember {
        mutableStateOf(false)
    }

    var subjectItem by remember {
        mutableStateOf(CommonR.string.subject)
    }

    var subjectClicked by remember {
        mutableStateOf(false)
    }

    val noteText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(stringResource(id = CommonR.string.note) + ": ")
        }

        append(stringResource(id = CommonR.string.for_not_technical_related_issues) + " ")

        pushStringAnnotation(
            tag = CommonR.string.general_contact_form.toString(),
            annotation = CommonR.string.general_contact_form.toString()
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = ColorPrimaryDark)) {
            append(stringResource(id = CommonR.string.general_contact_form).replace(".", ""))
        }

        append(".")
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->

            when (event) {
                is UiEvent.Success -> {
                    navController.navigate(Route.REPORT_SUCCESS)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .clickable {
                    subjectClicked = false
                }
                .padding(top = 120.h(), start = 32.w(), end = 32.w(), bottom = 55.h()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = CommonR.string.technical_issues) + "?",
                style = heading1TextStyle
            )

            Divider(color = Color.Transparent, modifier = Modifier.height(25.h()))

            Text(
                text = stringResource(id = CommonR.string.tech_issue_text),
                style = bodyRegularTextStyle.copy(
                    color = ColorTextPrimary,
                    letterSpacing = .5.sp,
                    textAlign = TextAlign.Center
                ),
            )

            Divider(color = Color.Transparent, modifier = Modifier.height(24.h()))


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        subjectClicked = !subjectClicked
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ), elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                ), border = BorderStroke(
                    width = 1.dp, color = if (selectSubjectWarning) {
                        Color.Red
                    } else {
                        Color.Transparent
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.w()),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = subjectItem),
                        style = if (subjectItem == CommonR.string.subject) {
                            bodyRegularTextStyle.copy(
                                color = if (selectSubjectWarning) {
                                    Color.Red
                                } else {
                                    ColorTextFieldPlaceholder
                                }
                            )
                        } else {
                            bodyRegularTextStyle.copy(
                                color = ColorTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = DesignSystemR.drawable.arrow_down_24),
                        contentDescription = ""
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
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
                        viewModel.onEvent(ReportProblemEvent.OnDescriptionEnter(it))
                        isDescriptionBlank = it.isEmpty()
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = CommonR.string.describe),
                            style = bodyRegularTextStyle.copy(color =if (isDescriptionBlank) Color.Red else ColorTextFieldPlaceholder)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(top = 10.dp)
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(15.dp))
                )

                if (subjectClicked) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, start = 5.dp, end = 5.dp)
                            .clickable {
                                subjectClicked = false
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                    ) {
                        repeat(REPORT_SUBJECT_LIST.size) { index ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        subjectItem = REPORT_SUBJECT_LIST[index]
                                        subjectClicked = false
                                        selectSubjectWarning = false
                                        viewModel.onEvent(
                                            ReportProblemEvent.OnSubjectSelect(
                                                context.resources.getString(
                                                    REPORT_SUBJECT_LIST[index]
                                                )
                                            )
                                        )
                                    }
                            ) {
                                Text(
                                    text = stringResource(id = REPORT_SUBJECT_LIST[index]),
                                    style = bodyRegularTextStyle.copy(
                                        color = ColorTextPrimary,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier
                                        .padding(15.dp)
                                )

                                if (index != REPORT_SUBJECT_LIST.size - 1) {
                                    Divider(
                                        color = Color.LightGray,
                                        thickness = 1.dp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }


            Divider(color = Color.Transparent, modifier = Modifier.height(25.h()))

            ClickableText(
                modifier = Modifier.padding(top = 25.h()),
                text = noteText,
                style = bodyRegularTextStyle.copy(
                    color = ColorTextPrimary,
                    letterSpacing = .5.sp,
                    textAlign = TextAlign.Center
                ), onClick = { offset ->
                    noteText.getStringAnnotations(
                        tag = CommonR.string.general_contact_form.toString(),
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        onGeneralClick()
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            AppActionButtonCompose(
                stringId = CommonR.string.report_problem,
                modifier = Modifier.padding(top = 20.h())
            ) {
                if (viewModel.state.description.isEmpty() || subjectItem == CommonR.string.subject) {

                    if (viewModel.state.description.isEmpty()) {
                        isDescriptionBlank = true
                    }

                    if (subjectItem == CommonR.string.subject) {
                        selectSubjectWarning = true
                    }

                } else {
                    viewModel.onEvent(ReportProblemEvent.OnSubmitClick)
                }

            }
        }

    }

    if (viewModel.state.isShowDialog)
        LoadingDialog {}
}

@Composable
@Preview
fun PreviewReportProblemScreen() {
    // ReportProblemScreen(onBack = {}, onReportSuccess = {})
}