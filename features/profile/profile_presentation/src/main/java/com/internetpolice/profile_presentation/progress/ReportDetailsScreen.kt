package com.internetpolice.profile_presentation.progress

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.internetpolice.core.common.util.FAV_ICON_PREFIX_URL
import com.internetpolice.core.designsystem.IpReportStatusType
import com.internetpolice.core.designsystem.R
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.profile_domain.model.VoteModel

@Composable
fun ReportDetailsScreen(
    voteModel: VoteModel,
    navController: NavController,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color.White), contentAlignment = Alignment.TopEnd
    ) {
        Column {
            AppToolbarCompose(
                title = stringResource(id = CommonR.string.back),
                modifier = Modifier.padding(top = 30.h(), start = 20.w(), end = 20.w())
            ) {
                navController.navigateUp()
            }
            Column(modifier = Modifier.padding(top = 30.h(), start = 35.w(), end = 35.w())) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = "$FAV_ICON_PREFIX_URL${voteModel.domainName}")
                                .apply(block = fun ImageRequest.Builder.() {
                                    error(R.drawable.ic_report_item)
                                }).build()
                        ),
                        contentDescription = "", modifier = Modifier
                            .padding(top = 2.h())
                            .size(20.r())
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = 5.h())
                            .width(8.w())
                    )
                    Text(
                        text = voteModel.domainName,
                        style = bodyBoldTextStyle,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = voteModel.createdDate, style = bodyXSRegularTextStyle
                )
                Row(
                    modifier = Modifier.padding(top = 26.h()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(
                            id = IpReportStatusType.valueOf(voteModel.voteStatus).iconResId
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(32.r())
                    )
                    Spacer(modifier = Modifier.width(12.w()))
                    Text(
                        text = stringResource(id = IpReportStatusType.valueOf(voteModel.voteStatus).reportStatusTypeNameStringRes),
                        style = bodyMediumTextStyle
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(top = 24.h(), bottom = 20.h())
                        .height(1.h())
                        .fillMaxWidth()
                        .scale(scaleY = 1f, scaleX = 2f)
                        .background(color = ColorDivider)
                )

                Text(text = stringResource(id = CommonR.string.details), style = heading1TextStyle)
                Text(
                    text = stringResource(id = CommonR.string.comment),
                    style = bodyMediumTextStyle,
                    modifier = Modifier.padding(top = 12.h())
                )
                ExpandableText(
                    text = voteModel.description,
                    textStyle =
                    bodyRegularTextStyle.copy(
                        color = ColorTextSecondary,
                        textAlign = TextAlign.Start
                    ),
                    seeMoreTextStyle = bodyBoldTextStyle.copy(color = ColorPrimaryDark),
                    minimizedMaxLines = 6
                )
                Text(
                    text = stringResource(id = CommonR.string.reported_as),
                    style = bodyMediumTextStyle,
                    modifier = Modifier.padding(top = 12.h(), bottom = 12.h())
                )
                ReportedAsItemCompose(voteModel.category)
                Text(
                    text = stringResource(id = CommonR.string.reported_status),
                    style = bodyMediumTextStyle,
                    modifier = Modifier.padding(top = 12.h(), bottom = 12.h())
                )
                Text(
                    text = stringResource(id = IpReportStatusType.valueOf(voteModel.voteStatus).reportStatusContent),
                    style = bodyRegularTextStyle.copy(
                        color = ColorTextSecondary,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.padding(bottom = 26.h())
                )

            }
        }
        Image(
            painter = painterResource(
                id = IpReportStatusType.valueOf(voteModel.voteStatus).imageResId
            ),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 100.h(), end = 35.w())
                .size(200.r())
        )
    }

}

@Composable
private fun ReportedAsItemCompose(category: String) {
    val context = LocalContext.current
    Card(
        colors = CardDefaults
            .elevatedCardColors(containerColor = Color.White),
        // elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .width((LocalConfiguration.current.screenWidthDp / 2.2).dp)
            .padding(top = 6.dp, bottom = 6.dp, end = 6.dp),
        border = BorderStroke(width = 1.w(), color = ColorPrimaryDark)

    ) {
        var reportCategory = reportCategories[0]
        reportCategories.forEach {
            if (context.getString(it.titleStringResId) == category) {
                reportCategory = it
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(18.w())

        ) {

            Image(
                painter = painterResource(id = reportCategory.iconRes),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(10.h()))
            Text(
                text = stringResource(id = reportCategory.titleStringResId),
                style = boldBodyTextStyle
            )


        }
    }
}

data class ReportCategory(
    @DrawableRes val iconRes: Int,
    @StringRes val titleStringResId: Int,
    val id: String,
)

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
@DevicePreviews
fun PreviewReportDetailApprovedScreen() {
//    ReportDetailsScreen(
//        comment = testLongText,
//        reportStatus =
//        "This report has been reviewed and confirmed by our team.",
//        onBack = {
//
//        }, reportStatusType = IpReportStatusType.APPROVED
//    )
}


@Composable
@Preview
fun PreviewReportDetailPendingScreen() {
//    ReportDetailsScreen(
//        comment = testLongText,
//        reportStatus =
//        "This report has been reviewed and confirmed by our team.", onBack = {
//
//        }, reportStatusType = IpReportStatusType.PENDING
//    )
}

@Composable
@Preview
fun PreviewReportDetailConfirmScreen() {
//    ReportDetailsScreen(
//        comment = testLongText, reportStatus =
//        "This report has been reviewed and confirmed by our team.", onBack = {
//
//        }, reportStatusType = IpReportStatusType.VERIFIED
//    )
}