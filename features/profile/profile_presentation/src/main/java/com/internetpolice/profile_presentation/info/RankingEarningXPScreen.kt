package com.internetpolice.profile_presentation.info

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.internetpolice.core.designsystem.IpRanksType
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.CUSTOM_LIGHT_GRAY
import com.internetpolice.core.designsystem.theme.ColorPrimaryDark
import com.internetpolice.core.designsystem.theme.ColorTextPrimary
import com.internetpolice.core.designsystem.theme.ITEM_DETAILED_COLOR
import com.internetpolice.core.designsystem.theme.bodyBoldTextStyle
import com.internetpolice.core.designsystem.theme.bodyLMediumTextStyle
import com.internetpolice.core.designsystem.theme.bodyLightTextStyle
import com.internetpolice.core.designsystem.theme.bodyMediumTextStyle
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.subHeading1TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.profile_domain.model.ContentList
import com.internetpolice.profile_domain.model.RankingEarningInfo
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun RankingEarningXPScreen(onBack: () -> Unit, viewModel: InfoViewModel = hiltViewModel()) {
    Box(
        modifier = Modifier
            .background(brush = AppBrush)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppToolbarCompose(
                title = stringResource(id = CommonR.string.back), modifier = Modifier.padding(
                    start = 20.w(),
                    top = 30.h(),
                    end = 20.w(),
                )
            ) {
                onBack()
            }

            Text(
                text = stringResource(id = CommonR.string.ranking),
                style = subHeading1TextStyle,
                modifier = Modifier.padding(vertical = 20.h())
            )

            Spacer(
                modifier = Modifier
                    .height(height = 1.dp)
                    .fillMaxWidth()
                    .background(color = Color.LightGray)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Color.White)
            ) {
                Spacer(modifier = Modifier.height(20.h()))

                RANKING_LIST.forEachIndexed { index, rankingEarningInfo ->
                    RankingItem(
                        index,
                        rankingEarningInfo,
                        IpRanksType.valueOf(viewModel.userRank.value).ordinal
                    )
                }
                Spacer(modifier = Modifier.height(20.h()))
            }

            Spacer(
                modifier = Modifier
                    .height(height = 1.dp)
                    .fillMaxWidth()
                    .background(color = Color.LightGray)
            )

            Text(
                text = stringResource(id = CommonR.string.earning_xp),
                style = subHeading1TextStyle,
                modifier = Modifier.padding(vertical = 20.h())
            )

            Spacer(
                modifier = Modifier
                    .height(height = 1.dp)
                    .fillMaxWidth()
                    .background(color = Color.LightGray)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = CUSTOM_LIGHT_GRAY),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = CommonR.string.reporting),
                    style = bodyBoldTextStyle,
                    modifier = Modifier
                        .padding(vertical = 10.h())
                        .padding(horizontal = 32.w())
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Color.White)
            ) {
                REPORTING_LIST.forEachIndexed { index, rankingEarningInfo ->
                    CommonItemForReportingInvitingCommunity(index, rankingEarningInfo)

                    Spacer(
                        modifier = Modifier
                            .height(height = 1.h())
                            .fillMaxWidth()
                            .background(color = Color.LightGray)
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = CUSTOM_LIGHT_GRAY),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = CommonR.string.inviting_suggesting),
                    style = bodyBoldTextStyle,
                    modifier = Modifier
                        .padding(vertical = 10.h())
                        .padding(horizontal = 32.w())
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Color.White)
            ) {
                INVITING_SUGGESTING_LIST.forEachIndexed { index, rankingEarningInfo ->
                    CommonItemForReportingInvitingCommunity(index, rankingEarningInfo)

                    Spacer(
                        modifier = Modifier
                            .height(height = 1.h())
                            .fillMaxWidth()
                            .background(color = Color.LightGray)
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = CUSTOM_LIGHT_GRAY),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = CommonR.string.contributing_to_the_community),
                    style = bodyBoldTextStyle,
                    modifier = Modifier
                        .padding(vertical = 10.h())
                        .padding(horizontal = 32.w())
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Color.White)
            ) {
                CONTRIBUTING_COMMUNITY_LIST.forEachIndexed { index, rankingEarningInfo ->
                    CommonItemForReportingInvitingCommunity(index, rankingEarningInfo)

                    Spacer(
                        modifier = Modifier
                            .height(height = 1.h())
                            .fillMaxWidth()
                            .background(color = Color.LightGray)
                    )
                }

            }

        }

    }
}


//#EBEBEB
@SuppressLint("UnrememberedMutableState")
@Composable
fun CommonItemForReportingInvitingCommunity(index: Int, rankingEarningInfo: RankingEarningInfo) {

    var showDetails by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = if (showDetails) ITEM_DETAILED_COLOR else Color.White),
    ) {

        Spacer(modifier = Modifier.height(20.h()))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.w()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = rankingEarningInfo.image!!),
                contentDescription = "",
                modifier = Modifier.size(25.r())
            )

            Text(
                text = rankingEarningInfo.xp!!,
                style = bodyBoldTextStyle,
                modifier = Modifier
                    .padding(horizontal = 8.w())
                    .width(55.w())
            )

            Text(
                text = stringResource(id = rankingEarningInfo.title!!),
                style = bodyMediumTextStyle.copy(
                    color = ColorPrimaryDark,
                    fontWeight = FontWeight.W500
                ),
                modifier = Modifier
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = if (showDetails) DesignSystemR.drawable.ic_minus_transparent_bg else DesignSystemR.drawable.ic_add_transapernt_bg),
                contentDescription = "",
                modifier = Modifier
                    .size(25.r())
                    .clickable {
                        showDetails = !showDetails
                    }
            )
        }

        if (showDetails) {
            Text(
                text = stringResource(id = rankingEarningInfo.contentList!![0].text!!),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.w())
                    .padding(top = 16.h()),
                style = bodyRegularTextStyle.copy(
                    textAlign = TextAlign.Justify,
                    fontWeight = FontWeight(500),
                )
            )
        }

        Spacer(modifier = Modifier.height(20.h()))
    }

}

@Composable
fun RankingItem(index: Int, rankingEarningInfo: RankingEarningInfo, myPosition: Int) {
    val itemPadding = if (index != RANKING_LIST.size - 1) 12 else 0
    val alpha = if (index == myPosition) 1F else .2F

    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = itemPadding.h()),
        ) {
            Column(
                modifier = Modifier.padding(start = 32.w(), end = 40.w()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Image(
                    painter = painterResource(id = rankingEarningInfo.image!!),
                    contentDescription = "",
                    modifier = Modifier
                        .width(85.w())
                        .height(25.h()),
                    alpha = alpha
                )

                Spacer(modifier = Modifier.height(12.h()))

                Text(
                    text = stringResource(id = rankingEarningInfo.rank!!),
                    style = bodyLMediumTextStyle.copy(
                        color = ColorTextPrimary.copy(alpha = alpha),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .height(25.h())

                )

            }

            Column(
                modifier = Modifier.padding(end = 32.w()),
            ) {

                Text(
                    text = rankingEarningInfo.xp!!,
                    style = bodyLMediumTextStyle.copy(
                        color = ColorTextPrimary.copy(alpha = alpha),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .height(25.h())
                )
                Spacer(modifier = Modifier.height(12.h()))

                Column(modifier = Modifier.fillMaxWidth()) {
                    rankingEarningInfo.contentList!!.forEachIndexed { _, contentList ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(25.h()),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = contentList.icon!!),
                                contentDescription = "",
                                tint = ColorPrimaryDark.copy(alpha = alpha)
                            )
                            Spacer(modifier = Modifier.width(12.w()))
                            Text(
                                text = stringResource(id = contentList.text!!),
                                style = bodyLightTextStyle.copy(ColorTextPrimary.copy(alpha = alpha))
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.r()))
                    }
                }

            }
        }
    }
}

@Composable
@Preview
fun PreviewRankingEarningXPScreen() {
    RankingEarningXPScreen(onBack = {})
}

@Composable
@Preview
fun PreviewRankingItem() {
    RankingItem(
        index = 2,
        rankingEarningInfo = RankingEarningInfo(
            image = IpRanksType.valueOf(IpRanksType.LIEUTENANT.toString()).iconResId,
            rank = IpRanksType.valueOf(IpRanksType.LIEUTENANT.toString()).rankNameStringRes,
            xp = "10,000+ XP",
            contentList = listOf(
                ContentList(
                    icon = DesignSystemR.drawable.ic_checked,
                    text = CommonR.string.free_family_subscription
                ),
                ContentList(
                    icon = DesignSystemR.drawable.ic_checked,
                    text = CommonR.string.community_moderator
                ),
                ContentList(
                    icon = DesignSystemR.drawable.ic_checked,
                    text = CommonR.string.official_advisor_for_internet_police
                )
            )
        ),
        myPosition = 2
    )
}