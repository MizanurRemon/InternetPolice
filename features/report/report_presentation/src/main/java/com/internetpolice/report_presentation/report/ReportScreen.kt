package com.internetpolice.report_presentation.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.noRippleClickable
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.onboardings_presentation.DotsCompose
import com.internetpolice.report_presentation.ReportViewModel
import kotlinx.coroutines.launch
import com.internetpolice.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ReportScreen(
    onBack: () -> Unit,
    onFinished: () -> Unit,
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
                    DomainSearchScreen(onBack = onBack,
                        viewModel = viewModel,
                        isAnyItemSelected = {
                            isAnyItemSelected.value = it
                        })
                }
                1 -> {
                    ReportTypeSelectionScreen(onBack = onBack, viewModel = viewModel,
                        isAnyItemSelected = {
                            isAnyItemSelected.value = it
                        })
                }
                2 -> {
                    ReportCategorizeScreen(
                        viewModel = viewModel,
                        onBack = onBack
                    ) {
                        isAnyItemSelected.value = it
                    }
                }
                else -> {

                }
            }
        }
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
                        }
                    }
            )
            DotsCompose(
                pageState.currentPage,
                size = 8.r(),
                totalDocs = totalPage,
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
    ReportScreen(onBack = {

    }, onFinished = {

    })
}
