package com.internetpolice.report_presentation.report

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.report_presentation.ReportEvent
import com.internetpolice.report_presentation.ReportViewModel
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun ReportCategorizeScreen(
    viewModel: ReportViewModel,
    onBack: () -> Unit, isAnyItemSelected: (Boolean) -> Unit,
) {

    val selectedCategoryIdList = remember {
        mutableStateListOf<String>()
    }
    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .padding(top = 30.h(), start = 16.w(), end = 16.w()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppToolbarCompose(title = stringResource(id = CommonR.string.home)) {
            onBack()
        }
        Spacer(modifier = Modifier.height(100.h()))
        Text(
            text = stringResource(id = CommonR.string.how_would_you_categorize_it),
            style = heading1TextStyle.copy(color = ColorTextPrimary)
        )
        Spacer(modifier = Modifier.height(40.h()))
        Text(
            text = stringResource(id = CommonR.string.press_and_hold_for_more_info),
            style = bodyRegularTextStyle.copy(color = ColorTextSecondary)
        )
        Spacer(modifier = Modifier.height(30.h()))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 160.h())
        ) {
            items(viewModel.state.reportTypeList.size) { index ->
                ReportCategoryItemCompose(
                    ReportCategory(
                        iconRes = viewModel.state.reportTypeList[index].iconRes,
                        titleStringResId = viewModel.state.reportTypeList[index].titleStringResId,
                        id = viewModel.state.reportTypeList[index].id
                    ),
                    selectedCategoryIdList = selectedCategoryIdList,
                ) { selectedCategoryId ->
                    if (selectedCategoryIdList.contains(selectedCategoryId)) {
                        selectedCategoryIdList.remove(selectedCategoryId)
                    } else {
                        selectedCategoryIdList.clear()
                        selectedCategoryIdList.add(selectedCategoryId)
                    }
                    viewModel.onEvent(ReportEvent.OnSelectCategory(selectedCategoryId))
                    isAnyItemSelected(selectedCategoryIdList.isNotEmpty())
                }
            }
        }
    }
}

val reportCategories = listOf(
    ReportCategory(
        iconRes = DesignSystemR.drawable.ic_phishing,
        titleStringResId = CommonR.string.phishing, id = "1"
    ),
    ReportCategory(
        iconRes = DesignSystemR.drawable.ic_shopping,
        titleStringResId = CommonR.string.shopping_or_auction, id = "2"
    ),
    ReportCategory(
        iconRes = DesignSystemR.drawable.ic_advance_fee,
        titleStringResId = CommonR.string.advance_fee_fraud, id = "3"
    ),
    ReportCategory(
        iconRes = DesignSystemR.drawable.ic_bit_coin,
        titleStringResId = CommonR.string.crypto, id = "4"
    ),
    ReportCategory(
        iconRes = DesignSystemR.drawable.ic_lottery,
        titleStringResId = CommonR.string.lottery, id = "5"
    ),
    ReportCategory(
        iconRes = DesignSystemR.drawable.ic_charity,
        titleStringResId = CommonR.string.charity, id = "6"
    ),
    ReportCategory(
        iconRes = DesignSystemR.drawable.ic_dating,
        titleStringResId = CommonR.string.dating_and_romance, id = "7"
    ),
    ReportCategory(
        iconRes = DesignSystemR.drawable.ic_others,
        titleStringResId = CommonR.string.others, id = "8"
    ),
)

@Composable
fun ReportCategoryItemCompose(
    reportCategory: ReportCategory,
    selectedCategoryIdList: List<String> = emptyList(),
    onSelectedUid: (String) -> Unit,
) {

    val context = LocalContext.current

    val isSelected =
        selectedCategoryIdList.any { it == context.getString(reportCategory.titleStringResId) }
    Card(
        colors = CardDefaults
            .elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(6.r()),
        border = if (isSelected)
            BorderStroke(width = 2.dp, color = ColorPrimaryDark)
        else null
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .noRippleClickable {
                    onSelectedUid(context.getString(reportCategory.titleStringResId))
                }
        ) {
            Spacer(modifier = Modifier.height(18.h()))
            Image(
                painter = painterResource(id = reportCategory.iconRes),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(10.h()))
            Text(
                text = stringResource(id = reportCategory.titleStringResId),
                style = boldBodyTextStyle
            )
            Spacer(modifier = Modifier.height(18.h()))

        }
    }
}

data class ReportCategory(
    @DrawableRes val iconRes: Int,
    @StringRes val titleStringResId: Int,
    val id: String,
)


@Composable
@DevicePreviews
fun PreviewReportCategorizeScreen() {
//    ReportCategorizeScreen(onBack = {
//
//    }, isAnyItemSelected = {
//
//    })
}