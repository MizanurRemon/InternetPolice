package com.internetpolice.onboardings_presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.ColorPrimaryDark
import com.internetpolice.core.designsystem.theme.ColorPrimaryLight
import com.internetpolice.core.designsystem.theme.ColorTextSecondary
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.conditional
import com.internetpolice.core.designsystem.theme.heading1TextStyle
import com.internetpolice.core.designsystem.theme.noRippleClickable
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import kotlinx.coroutines.launch
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun OnBoardingScreen(
    onBoardingItem: OnBoardingItem,
    index: Int,
    onClick: (index: Int) -> Unit
) {
    ConstraintLayout(modifier = Modifier.background(brush = AppBrush)) {
        val (toolbar, coverImage, bottomInfo) = createRefs()
        AppToolbarCompose(title = stringResource(CommonR.string.not_implemented),
            modifier = Modifier
                .padding(start = 30.w(), top = 30.h())
                .constrainAs(toolbar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
            onClick(index - 1)
        }
        Image(
            painter = painterResource(id = onBoardingItem.drawableId),
            contentDescription = "",
            modifier = Modifier
                .padding(start = 20.w(), end = 20.w(), top = 20.h())
                .width(372.w())
                .height(360.h())
                .constrainAs(coverImage) {
                    bottom.linkTo(bottomInfo.top)
                    top.linkTo(toolbar.bottom)
                },
        )
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(topStart = 35.r(), topEnd = 35.r()),
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomInfo) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(367.h())
            ) {
                Column {
                    Text(
                        text = stringResource(id = onBoardingItem.titleStringId),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.h(), bottom = 20.h()),
                        textAlign = TextAlign.Center,
                        style = heading1TextStyle
                    )
                    Text(
                        text = stringResource(id = onBoardingItem.subTitleStringId),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.w(), end = 32.w()),
                        style = bodyRegularTextStyle.copy(color = ColorTextSecondary)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(50.h()))
                }
            }
        }
    }

}

@Composable
private fun RightArrow(
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = DesignSystemR.drawable.ic_righ_arrow_with_bg),
        contentDescription = "",
        modifier = Modifier
            .size(64.w())
            .noRippleClickable { onClick() }
    )
}

@Composable
private fun LeftArrow(index: Int, onClick: () -> Unit) {
    if (index == 0)
        Spacer(modifier = Modifier.size(64.w()))
    else {
        Image(
            painter = painterResource(id = DesignSystemR.drawable.ic_left_arrow_without_bg),
            contentDescription = "",
            modifier = Modifier
                .size(64.r())
                .padding(20.r())
                .noRippleClickable { onClick() }
        )
    }
}

@ExperimentalPagerApi
@Composable
fun InitOnboardingScreen(navController: NavController) {
    val items = OnBoardingItem.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    val onLeftClick: () -> Unit = {
        scope.launch {
            if (pageState.currentPage > 0) {
                selectedIndex--
                pageState.animateScrollToPage(pageState.currentPage - 1)
            }else navController.navigateUp()
        }
    }
    val onRightClick: () -> Unit = {
        scope.launch {
            if (pageState.currentPage == 2) {
                navController.navigate(Route.PERSONALISE_PROFILE_ONBOARDING)
            } else if (pageState.currentPage < 2) {
                selectedIndex++
                pageState.animateScrollToPage(pageState.currentPage + 1)
            }
        }
    }
    Column(
        modifier = Modifier
            .background(brush = AppBrush)
    ) {
        HorizontalPager(
            count = items.size,
            state = pageState,
            //modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top,
            userScrollEnabled = false
        ) { pageIndex ->
            OnBoardingScreen(items[pageIndex], index = pageIndex, onClick = {
                onLeftClick()
            })
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(start = 32.w(), end = 32.w(), bottom = 60.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LeftArrow(selectedIndex, onClick = onLeftClick)
            DotsCompose(
                selectedIndex,
                totalDocs = 3, Modifier.weight(1F),
                ColorPrimaryLight
            )
            RightArrow(onRightClick)
        }
    }
}

@Composable
fun DotsCompose(
    index: Int,
    totalDocs: Int,
    modifier: Modifier,
    disableDotColor: Color = ColorPrimaryLight,
    size: Dp = 12.r(),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until totalDocs) Box(
            modifier = Modifier
                .padding(start = 4.w(), end = 4.w())
                .size(size)
                .conditional(i == index) {
                    return@conditional background(
                        color = ColorPrimaryDark,
                        shape = CircleShape
                    )
                }
                .conditional(i != index) {
                    background(
                        color = disableDotColor,
                        shape = CircleShape
                    )
                }

        )
    }
}

@Composable
@DevicePreviews
fun PreviewOnboardingScreen() {
    OnBoardingScreen(
        OnBoardingItem(
            DesignSystemR.drawable.ic_onboard_1,
            CommonR.string.onBoardingTitle1,
            CommonR.string.onBoardingSubTitle1
        ),
        1, onClick = {
        }
    )
}

@ExperimentalPagerApi
@Composable
@DevicePreviews
fun PreInitOnboardingScreen() {
    val navController: NavHostController = rememberNavController()
    InitOnboardingScreen(navController)
}