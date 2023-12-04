package com.internetpolice.report_presentation.screen_website

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.noRippleClickable
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.onboardings_presentation.DotsCompose
import com.internetpolice.report_domain.model.DomainModel
import com.internetpolice.report_presentation.ReportEvent
import com.internetpolice.report_presentation.ReportViewModel
import com.internetpolice.report_presentation.report.ReportCategorizeScreen
import com.internetpolice.report_presentation.report.ReportDescriptionInputScreen
import com.internetpolice.report_presentation.report.ReportTypeSelectionScreen
import kotlinx.coroutines.launch
import com.internetpolice.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ReportWebsiteScreen(
    domainModel: DomainModel,
    onBack: () -> Unit,
    onFinished: () -> Unit,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: ReportViewModel = hiltViewModel(),

    ) {
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()
    val isAnyItemSelected = remember {
        mutableStateOf(false)
    }
    val totalPage = 3
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
    ) {
        HorizontalPager(
            count = totalPage,
            state = pageState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { pageIndex ->
            return@HorizontalPager when (pageIndex) {
                0 -> {
                    viewModel.onEvent(ReportEvent.OnSelectedDomain(domainModel))
                    ReportTypeSelectionScreen(onBack = onBack, viewModel = viewModel,
                        isAnyItemSelected = {
                            isAnyItemSelected.value = it
                        })
                }
                1 -> {
                    ReportCategorizeScreen(
                        viewModel = viewModel,
                        onBack = onBack
                    ) {
                        isAnyItemSelected.value = it
                    }
                }
                2 -> {
                    ReportDescriptionInputScreen(
                        viewModel, navController, snackbarHostState
                    )
                }
                else -> {
                }
            }
        }
        if (pageState.currentPage < 2)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent)
                    .padding(bottom = 55.h())
                    .align(Alignment.BottomCenter)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = DesignSystemR.drawable.ic_left_arrow_rounded_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.r())
                        .noRippleClickable {
                            scope.launch {
                                if (pageState.currentPage > 0)
                                    pageState.animateScrollToPage(pageState.currentPage - 1)
                                else if (pageState.currentPage == 0)
                                    onBack()
                            }
                        }
                )
                DotsCompose(
                    pageState.currentPage,
                    size = 8.r(),
                    totalDocs = 2,
                    disableDotColor = Color(0x40091B3D),
                    modifier = Modifier
                )
                Image(
                    painter = painterResource(
                        id =
                        if (isAnyItemSelected.value) DesignSystemR.drawable.ic_right_arrow_rounded_bg_enable
                        else DesignSystemR.drawable.ic_right_arrow_rounded_bg_disable
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.r())
                        .noRippleClickable {
                            if (isAnyItemSelected.value) {
                                isAnyItemSelected.value = false
                                scope.launch {
                                    if (pageState.currentPage == 2) {
                                        onFinished()
                                    } else if (pageState.currentPage < 3)
                                        pageState.animateScrollToPage(pageState.currentPage + 1)

                                }
                            }
                        }
                )
            }
    }
}

@Composable
@DevicePreviews
fun PreviewReportScreen() {
//    ReportWebsiteScreen(onBack = {
//
//    }, onFinished = {
//
//    })
}
