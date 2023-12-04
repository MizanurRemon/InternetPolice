package com.internetpolice.settings_presentation.screens.faq

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.settings_presentation.helpers.ScreenSize
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FAQScreen(
    onBack: () -> Unit,
    videoOnboarding: (Any?) -> Unit,
    viewModel: FaqViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AppToolbarCompose(
            title = stringResource(
                id = CommonR.string.back
            ),
            modifier = Modifier.padding(start = 16.w(), top = 24.h(), end = 16.w(), bottom = 50.h())
        ) {
            onBack()
        }

        Image(
            painter = painterResource(id = DesignSystemR.drawable.ic_faq_top),
            contentDescription = "",
            modifier = Modifier
                .height(280.dp)
                .padding(0.dp),
        )

        Divider(
            thickness = 20.h(),
            color = Color.White
        )


        if (viewModel.state.isLoading) {
            CircularProgressIndicator(
                strokeWidth = 4.dp,
                color = ColorPrimaryDark,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xffF6FCFC)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(viewModel.state.questionList.size) { index ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                    ) {
                        var showDetails by remember {
                            mutableStateOf(false)
                        }
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showDetails = !showDetails
                            }) {


                            ConstraintLayout(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.w())
                            ) {
                                val (icon, titleText) = createRefs()
                                Text(
                                    text = viewModel.state.questionList[index].question,
                                    style = bodyMediumTextStyle.copy(
                                        color = ColorPrimaryDark
                                    ),
                                    modifier = Modifier
                                        .constrainAs(titleText) {
                                            start.linkTo(parent.start)
                                            top.linkTo(parent.top)
                                            bottom.linkTo(parent.bottom)
                                            end.linkTo(icon.start)
                                            width = Dimension.fillToConstraints
                                        }
                                        .padding(end = 10.dp)
                                )

                                Icon(
                                    if (showDetails) painterResource(id = DesignSystemR.drawable.arrow_up_24) else painterResource(
                                        id = DesignSystemR.drawable.arrow_down_24
                                    ),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(30.r())
                                        .constrainAs(icon) {
                                            top.linkTo(parent.top)
                                            end.linkTo(parent.end)
                                        },
                                    tint = Color.Gray
                                )
                            }

                            if (showDetails) {
                                HtmlToTextWithImage(
                                    text = viewModel.state.questionList[index].answer,
                                    modifier = Modifier.padding(
                                        bottom = 15.h(),
                                        start = 15.w(),
                                        end = 15.w()
                                    )
                                )
                            }
                            Divider(color = Color.LightGray)
                        }
                    }

                }
            }
        }

    }

}

@Composable
@DevicePreviews
fun PreviewFaqScreen() {
    FAQScreen(onBack = {}, videoOnboarding = {})
}

