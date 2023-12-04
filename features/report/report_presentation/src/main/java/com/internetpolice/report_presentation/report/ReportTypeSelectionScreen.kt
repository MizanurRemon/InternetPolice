package com.internetpolice.report_presentation.report

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.w
import com.internetpolice.report_presentation.ReportEvent
import com.internetpolice.report_presentation.ReportViewModel

@Composable
fun ReportTypeSelectionScreen(
    viewModel: ReportViewModel,
    onBack: () -> Unit, isAnyItemSelected: (Boolean) -> Unit,
) {
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
            text = stringResource(id = CommonR.string.i_think_this_website_is),
            style = heading1TextStyle.copy(color = ColorTextPrimary)
        )
        Spacer(modifier = Modifier.height(40.h()))
        Text(
            text = stringResource(id = CommonR.string.please_choose),
            style = bodyRegularTextStyle.copy(color = ColorTextSecondary)
        )
        Spacer(modifier = Modifier.height(30.h()))
        ReportTypesCompose(
            reportTypeList = reportTypeList,
            isAnyItemSelected = isAnyItemSelected,
            viewModel= viewModel,

            )
    }
}

@Composable
fun ReportTypesCompose(
    reportTypeList: List<ReportType>,
    isAnyItemSelected: (Boolean) -> Unit,
    viewModel: ReportViewModel,

    ) {

    val selectedTypeId = remember {
        mutableStateOf("")
    }

    Column(
    ) {
        ReportTypeSelectionCompose(
            reportTypeList[0],
            selectedTypeId = selectedTypeId,
        ) {
            selectedTypeId.value = it
            viewModel.onEvent(ReportEvent.OnSelectedReportType("Suspicious"))
            isAnyItemSelected(it.isNotBlank())
        }
        ReportTypeSelectionCompose(
            reportTypeList[1],
            selectedTypeId = selectedTypeId,
        ) {
            selectedTypeId.value = it
            viewModel.onEvent(ReportEvent.OnSelectedReportType("Malicious"))
            isAnyItemSelected(it.isNotBlank())
        }
    }

}

@Composable
@Preview
fun PreviewReportTypeSelectionScreen() {
//    ReportTypeSelectionScreen(
//        view,onBack = {
//    }, isAnyItemSelected = {
//    })
}

val reportTypeList = listOf(
    ReportType(
        iconRes = DesignSystemR.drawable.ic_rating_medium,
        name = CommonR.string.suspicious, id = "1"
    ),
    ReportType(
        iconRes = DesignSystemR.drawable.ic_rating_low,
        name = CommonR.string.malicious, id = "2"
    ),
)

@Composable
fun ReportTypeSelectionCompose(
    reportType: ReportType,
    selectedTypeId: MutableState<String>,
    onSelectedUid: (String) -> Unit,
) {

    val isSelected = selectedTypeId.value == reportType.id

    Card(
        colors = CardDefaults
            .elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(6.r())
            .noRippleClickable {
                if (isSelected) onSelectedUid("")
                else onSelectedUid(reportType.id)
            },
        border = if (isSelected)
            BorderStroke(width = 2.w(), color = ColorPrimaryDark)
        else null
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.w(), top = 24.h(), bottom = 24.h())
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = reportType.iconRes),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.w()))
            Text(
                text = stringResource(id = reportType.name),
                style = boldBodyTextStyle
            )
        }
    }
}

data class ReportType(
    @DrawableRes val iconRes: Int,
    @StringRes val name: Int,
    val id: String,
)