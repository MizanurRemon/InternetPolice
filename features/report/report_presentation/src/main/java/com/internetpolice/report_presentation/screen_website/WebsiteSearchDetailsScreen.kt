package com.internetpolice.report_presentation.screen_website

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.internetpolice.core.common.util.FAV_ICON_PREFIX_URL
import com.internetpolice.core.common.util.SpecialTagsFromServer
import com.internetpolice.core.common.util.capitalizeFirstCharacter
import com.internetpolice.core.designsystem.components.*
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.report_domain.model.DomainModel
import com.internetpolice.report_domain.model.VoteDescriptionModel
import com.internetpolice.report_presentation.ReportDetailsViewModel
import com.internetpolice.report_presentation.ScoreType
import com.internetpolice.report_presentation.di.ViewModelFactoryProvider
import com.internetpolice.report_presentation.getScoreType
import dagger.hilt.android.EntryPointAccessors
import de.charlex.compose.HtmlText
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun WebsiteSearchDetailsScreen(
    domainModel: DomainModel,
    onBack: () -> Unit,
    onNavigate: (DomainModel) -> Unit,
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity, ViewModelFactoryProvider::class.java
    ).mainViewModelFactory()

    val reportDetailsViewModel: ReportDetailsViewModel = viewModel(
        factory = ReportDetailsViewModel.provideMainViewModelFactory(
            factory, domainModel
        )
    )

    var showResult by remember { mutableStateOf(false) }
    var showReport by remember { mutableStateOf(false) }
    val scoreType = getScoreType(domainModel.trustScore)
    val state = reportDetailsViewModel.state

    val typeTitle = when (scoreType) {
        ScoreType.High -> stringResource(id = CommonR.string.website_seems_safe)
        ScoreType.Medium -> stringResource(id = CommonR.string.is_listed_as_suspicious)
        else -> stringResource(id = CommonR.string.is_listed_as_malicious)
    }.capitalizeFirstCharacter()

    Box(
        modifier = Modifier
            .background(brush = AppBrush)
            .padding(
                top = 30.h(),
                start = 16.w(),
                end = 16.w(),
            )
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppToolbarCompose(
                title = stringResource(id = CommonR.string.back), onBackClick = onBack
            )
            Spacer(modifier = Modifier.height(22.h()))

            CopActionCompose(
                title = domainModel.domain,
                actionTitle = typeTitle,
                bgId = when (scoreType) {
                    ScoreType.High -> DesignSystemR.drawable.ic_cop_high
                    ScoreType.Medium -> DesignSystemR.drawable.ic_cop_medium
                    else -> DesignSystemR.drawable.ic_cop_low
                }, actionButtonColor = when (scoreType) {
                    ScoreType.High -> Color(0xff469629)
                    ScoreType.Medium -> Color(0xffDC9605)
                    else -> Color(0xffEC3A00)
                },
                onActionButtonClick = {},
                favIconUrl = "${FAV_ICON_PREFIX_URL}${domainModel.domain}"
            )
            TrustScoreCompose(score = domainModel.trustScore)
            ResultCompose(
                trustScore = domainModel.trustScore,
                resultsList = state.resultMessageKeys,
            ) {
                showResult = it
            }
            if (showResult) ResultDetailsCompose(
                typeTitle, domainModel, results = state.resultMessageKeys
            )
            ReportCompose(reportList = state.voteList) {
                showReport = it
            }
            if (showReport) ReportDetailsCompose(
                reports = state.voteList
            )
            DoReportCompose {
                onNavigate(domainModel)
            }
            if (state.isShowLoading) LoadingDialog {}
        }

    }

    BackHandler {
        onBack()
    }
}


@Composable
@Preview
fun PreviewWebsiteSearchDetailsScreen() {
//    WebsiteSearchDetailsScreen(
//        params = testWebSearchDetailsScreenParams
//    ) {
//
//    }
}

@Composable
fun TrustScoreCompose(
    score: Double,
) {
    val scoreType = getScoreType(score)

    return Card(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 20.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {

        val scoreFormattedString = if (score % 1 == 0.0) {
            "%.0f".format(score)
        } else {
            "%f".format(score)
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(
                    id = when (scoreType) {
                        ScoreType.High -> DesignSystemR.drawable.ic_rating_high
                        ScoreType.Medium -> DesignSystemR.drawable.ic_rating_medium
                        else -> DesignSystemR.drawable.ic_rating_low
                    }
                ), contentDescription = null, modifier = Modifier.size(33.dp)
            )
            Text(
                text = stringResource(id = CommonR.string.trust_score),
                style = bodyBoldTextStyle,
                modifier = Modifier.padding(start = 16.w(), end = 5.w())
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .height(30.h())
                    .width(120.w())
                    .background(
                        brush = when (scoreType) {
                            ScoreType.High -> HighGreenBrush
                            ScoreType.Medium -> MediumOrangeBrush
                            else -> LowRedBrush
                        }
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${scoreType.name} $scoreFormattedString/100",
                    style = bodyXSBoldTextStyle.copy(textAlign = TextAlign.Center),
                )
            }
        }
    }
}

@Composable
fun ResultCompose(
    trustScore: Double,
    resultsList: List<String>,
    onAction: (Boolean) -> Unit,
) {
    var isExtended by remember { mutableStateOf(false) }
    return Card(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {


        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(
                    id = if (trustScore < 70) DesignSystemR.drawable.ic_result_red
                    else DesignSystemR.drawable.ic_result_blue
                ), contentDescription = null, modifier = Modifier.size(33.r())
            )
            Spacer(modifier = Modifier.width(16.w()))
            Text(
                text = resultsList.size.toString() + " " + stringResource(id = CommonR.string.result),
                style = bodyBoldTextStyle
            )
            Spacer(modifier = Modifier.weight(1f))

            if (resultsList.isNotEmpty()) Image(painter = painterResource(
                id = if (isExtended) DesignSystemR.drawable.ic_collapse
                else DesignSystemR.drawable.ic_extend
            ), contentDescription = null, modifier = Modifier
                .size(28.dp)
                .clickable {
                    isExtended = !isExtended
                    onAction(isExtended)
                })
        }
    }
}

@Composable
fun ResultDetailsCompose(
    typeTitle: String,
    domainModel: DomainModel,
    results: List<String>,
) {
    val modifier = Modifier
        .padding(top = 8.dp, bottom = 8.dp)
        .wrapContentHeight()
        .fillMaxWidth()

    return Card(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 24.dp, vertical = 18.dp
            )
        ) {
            Text(
                text = "${domainModel.domain} $typeTitle",
                style = bodyBoldTextStyle.copy(
                    color = Color(0xff5A6BA4)
                ),
            )
            Column(modifier = Modifier) {
                repeat(results.size) { index ->
                    Column(
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = if (domainModel.trustScore >= 70) DesignSystemR.drawable.ic_positive else DesignSystemR.drawable.ic_negative),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(12.w()))

                            HtmlText(
                                text = stringResource(
                                    id = SpecialTagsFromServer.valueOf(
                                        results[index].replace(".", "_").uppercase()
                                    ).value
                                ), style = bodyXSRegularTextStyle, urlSpanStyle = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = fontRoboto,
                                    fontSize = 12.ssp(),
                                    color = Color(0xff3E6FCC),
                                )
                            )

                        }
                        if (index != results.size - 1) Spacer(modifier = Modifier.height(6.dp))
                        if (index != results.size - 1) Box(
                            modifier = Modifier
                                .height(0.7.dp)
                                .fillMaxWidth()
                                .background(color = Color(0xffBFCEFF))
                        )

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReportDetailsCompose(
    reports: List<VoteDescriptionModel>,
) {
    val modifier = Modifier
        .padding(top = 8.dp, bottom = 8.dp)
        .height(IntrinsicSize.Max)
        .fillMaxWidth()
    return Column(
        modifier = Modifier.padding(
            vertical = 18.dp,
        )

    ) {
        repeat(reports.size) { index ->
            Card(
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = modifier
            ) {
                val report = reports[index]
                Row(modifier = Modifier
                    .background(brush = AppBrush)
                    .conditional(condition = report.voteType == ReportCategory.SUSPICIOUS.name) {
                        return@conditional background(brush = SuspiciousBrush)
                    }
                    .conditional(condition = report.voteType == ReportCategory.MALICIOUS.name) {
                        return@conditional background(brush = MaliciousBrush)
                    }
                    .fillMaxWidth()) {
                    Spacer(modifier = Modifier
                        .width(14.w())
                        .conditional(condition = report.voteType == ReportCategory.SUSPICIOUS.name) {
                            return@conditional background(Color(0xffFFAC00))
                        }
                        .conditional(condition = report.voteType == ReportCategory.MALICIOUS.name) {
                            return@conditional background(Color(0xffFA3D00))
                        }
                        .fillMaxHeight())
                    Spacer(modifier = Modifier.width(20.w()))
                    ProfileImageCompose(
                        score = 20,
                        profileImageUrl = reports[index].avatarImagePath,
                        enablePress = false,
                        size = 80,
                        modifier = Modifier.padding(5.dp),
                        isGuest = false, onEditClick = {
                            
                        }
                    )
                    Spacer(modifier = Modifier.width(20.w()))
                    Column(
                        modifier = Modifier.padding(
                            vertical = 18.h(), horizontal = 18.w()
                        )
                    ) {
                        Row {
                            Text(
                                text = report.voteType,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.5.dp))
                                    .background(
                                        color = when (report.voteType) {
                                            ReportCategory.MALICIOUS.name -> Color(0xffFFF0D0)
                                            ReportCategory.SUSPICIOUS.name -> Color(0xffFFEBE4)
                                            else -> {
                                                Color(0xffFFEBE4)
                                            }
                                        }
                                    )
                                    .padding(5.dp),
                                style = bodyXXSBoldTextStyle.copy(
                                    color = if (report.voteType == ReportCategory.MALICIOUS.name) Color(
                                        0xffFFAC00
                                    )
                                    else Color(0xffFA3D00),

                                    )
                            )
                            Spacer(modifier = Modifier.width(6.w()))
                            Text(
                                text = report.category,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.5.dp))
                                    .background(Color(0xffEEF4FF))
                                    .padding(5.dp),
                                style = bodyXXSBoldTextStyle.copy(
                                    color = ColorTextPrimary,
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row {
                            Text(
                                text = report.nickname, style = bodyXSBoldTextStyle.copy(
                                    color = ColorTextPrimary,
                                )
                            )
                            Spacer(modifier = Modifier.width(6.w()))
                            Text(
                                text = " | " + report.rank, style = bodyXSRegularTextStyle.copy(
                                    color = ColorTextPrimary,
                                )
                            )
                        }
                        ExpandableText(text = report.description)

                    }

                }
            }
        }
    }
}


@Composable
fun ReportCompose(
    reportList: List<VoteDescriptionModel>,
    onAction: (Boolean) -> Unit,
) {
    var isExtended by remember { mutableStateOf(false) }

    return Card(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(
                    id = DesignSystemR.drawable.ic_megaphone
                ), contentDescription = null, modifier = Modifier.size(33.r())
            )
            Spacer(modifier = Modifier.width(16.w()))
            Text(
                text = reportList.size.toString() + " " + stringResource(id = CommonR.string.reports),
                style = bodyBoldTextStyle
            )
            Spacer(modifier = Modifier.weight(1f))
            if (reportList.isNotEmpty()) Image(painter = painterResource(
                id = if (isExtended) DesignSystemR.drawable.ic_collapse
                else DesignSystemR.drawable.ic_extend
            ), contentDescription = null, modifier = Modifier
                .size(28.dp)
                .clickable {
                    isExtended = !isExtended
                    onAction(isExtended)
                })
        }
    }
}

@Composable
//@Preview
fun DoReportCompose(
    onNavigate: () -> Unit,
) {

    return Card(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = CommonR.string.report_this_website),
                textAlign = TextAlign.Center,
                style = subHeading1TextStyle.copy(color = ColorTextPrimary)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(id = CommonR.string.report_this_website_info),
                textAlign = TextAlign.Center,
                style = bodyRegularTextStyle.copy(color = ColorTextSecondary)
            )
            Spacer(modifier = Modifier.height(24.dp))
            AppActionButtonCompose(
                stringId = CommonR.string.yes_report_this_website_info,
                bgColor = ColorPrimaryDark,
                textColor = Color.White
            ) {
                onNavigate()
            }

        }
    }
}

@Composable
@Preview
fun PreviewTopMedium() {
    CopActionCompose(
        title = "www.github.com.phishing.protocol.pack.org.in.indico",
        actionTitle = "This website is malicious website",
        bgId = DesignSystemR.drawable.ic_cop_medium,
        actionButtonColor = Color(0xffDC9605),
        onActionButtonClick = { },
        favIconUrl = ""
    )
}

@Composable
@Preview
fun PreviewTopHigh() {
    CopActionCompose(
        title = "..Github.com",
        actionTitle = stringResource(id = CommonR.string.website_seems_safe),
        bgId = DesignSystemR.drawable.ic_cop_high,
        actionButtonColor = Color(0xff469629),
        onActionButtonClick = {},
        favIconUrl = ""
    )
}

@Composable
@Preview
fun PreviewTopLow() {
    CopActionCompose(
        title = "..Github.com",
        actionTitle = stringResource(id = CommonR.string.is_listed_as_malicious),
        bgId = DesignSystemR.drawable.ic_cop_low,
        actionButtonColor = Color(0xffEC3A00),
        onActionButtonClick = { },
        favIconUrl = ""
    )
}

data class WebsiteSearchDetailsScreenParams(
    val websiteName: String,
    val actionTitle: String,
    @DrawableRes val topBannerBgID: Int,
    val topBannerActionColor: Color,
    val resultTitle: String = "",
    val results: List<Result> = emptyList(),
    val hasUserSubscription: Boolean = false,
    val score: Double,
    val reports: List<Report> = emptyList(),
)

data class Report(
    val reporterProfilePicUrl: String,
    val category: ReportCategory,
    val subCategory: ReportSubCategory?,
    val reportText: String,
    val designation: String,
    val reporterName: String,
)

enum class ReportCategory {
    SUSPICIOUS, MALICIOUS
}

enum class ReportSubCategory {
    PHISHING,
}

data class Result(val isPositive: Boolean = false, val resultText: String)