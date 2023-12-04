import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.components.InfoItemCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import kotlinx.coroutines.launch
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SubscriptionSelectionScreen(onFinish: () -> Unit) {
    val pageState = rememberPagerState()
    val totalPage = 4
    val selectedIndex = remember {
        mutableStateOf(0)
    }
    val scope = rememberCoroutineScope()

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBrush)
    ) {
        AppToolbarCompose(
            title =
            stringResource(id = CommonR.string.home),
            modifier = Modifier.padding(top = 30.h(), start = 20.w())
        ) {
        }
        HorizontalPager(
            count = totalPage,
            state = pageState,
            itemSpacing = 10.w(),
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.Top,
        ) { pageIndex ->
            return@HorizontalPager when (pageIndex) {
                0 -> {
                    SubscriptionCommonCompose(
                        titleResId = CommonR.string.basic,
                        price = 0.0,
                        infoList = basicSubscriptionInfoList,
                        backgroundTopImage = DesignSystemR.drawable.ic_basic_plan
                    ) {
                        onFinish()
                    }
                }
                1 -> {
                    SubscriptionCommonCompose(
                        titleResId = CommonR.string.premium,
                        price = 0.99,
                        backgroundTopImage = DesignSystemR.drawable.ic_premium,
                        infoList = premiumSubscriptionInfoList,
                    ) {
                        onFinish()
                    }
                }
                2 -> {
                    SubscriptionCommonCompose(
                        price = 4.99,
                        backgroundTopImage = DesignSystemR.drawable.ic_family_plan,
                        infoList = familySubscriptionInfoList,
                        titleResId = CommonR.string.family
                    ) {
                        onFinish()
                    }
                }
                else -> {
                    SubscriptionCommonCompose(
                        price = null,
                        backgroundTopImage = DesignSystemR.drawable.ic_business,
                        infoList = businessSubscriptionInfoList,
                        titleResId = CommonR.string.business
                    ) {
                        onFinish()
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 34.dp)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id =
                    if (selectedIndex.value == 0)
                        DesignSystemR.drawable.ic_left_arrow_rounded_bg_disable
                    else
                        DesignSystemR.drawable.ic_left_arrow_rounded_bg_enable
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(48.r())
                    .noRippleClickable {
                        scope.launch {
                            if (pageState.currentPage > 0) {
                                selectedIndex.value--
                                pageState.animateScrollToPage(pageState.currentPage - 1)
                            }
                        }
                    }
            )
            com.internetpolice.onboardings_presentation.DotsCompose(
                pageState.currentPage,
                size = 8.dp,
                totalDocs = totalPage,
                disableDotColor = Color(0x40091B3D),
                modifier = Modifier
            )
            Image(
                painter = painterResource(
                    id =
                    if (selectedIndex.value == 3)
                        DesignSystemR.drawable.ic_right_arrow_rounded_bg_disable
                    else
                        DesignSystemR.drawable.ic_right_arrow_rounded_bg_enable
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .noRippleClickable {
                        scope.launch {
                            if (pageState.currentPage == 3) {
                                // onFinished()
                            } else if (pageState.currentPage < 3) {
                                selectedIndex.value++
                                pageState.animateScrollToPage(pageState.currentPage + 1)
                            }
                        }
                    }
            )
        }
    }
}

@Composable
fun SubscriptionCommonCompose(
    price: Double?,
    @DrawableRes backgroundTopImage: Int,
    infoList: List<SubscriptionInfoItem>,
    @StringRes titleResId: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 44.w(), top = 20.h(), end = 44.w())
            .fillMaxSize()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                clip = true,
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color(0xffF6F8FF))
                    .wrapContentSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painterResource(id = backgroundTopImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.4f)
                        .padding(top = 50.h()),
                    contentScale = ContentScale.Crop
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 20.h())
                ) {
                    Text(
                        text = stringResource(id = titleResId),
                        style = subHeading1TextStyle.copy(
                            color = ColorTextPrimary,
                            letterSpacing = 5.ssp()
                        )
                    )
                    Spacer(modifier = Modifier.fillMaxHeight(0.32f))
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text =
                            if (price == null)
                                stringResource(id = CommonR.string.lets_talk)
                            else
                                "€ $price",
                            style = heading1TextStyle,
                            modifier = Modifier.padding(
                                bottom =
                                if (price == null)
                                    20.dp
                                else 0.dp,
                            )
                        )
                        if (price != null) {
                            if (price > 0.0)
                                Text(
                                    text = " / " + stringResource(id = CommonR.string.month),
                                    style = bodyLMediumTextStyle.copy(color = Color(0xff3E6FCC))
                                )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(top = 10.h()))
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                repeat(infoList.size) {
                    InfoItemCompose(
                        titleStringResId = infoList[it].infoStringResId,
                        iconResId = infoList[it].drawableId,
                        modifier = Modifier
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 10.h())
            )
            if (price != null) {
                if (price > 0.0)
                    AppActionButtonCompose(
                        stringId = CommonR.string.purchase,
                        modifier = Modifier
                            .padding(start = 20.w(), end = 20.w(), bottom = 20.h())
                            .fillMaxWidth()
                    ) {
                        onClick()
                    }
            } else {
                AppActionButtonCompose(
                    stringId = CommonR.string.contact_us,
                    modifier = Modifier
                        .padding(start = 20.w(), end = 20.w(), bottom = 20.h())
                        .fillMaxWidth()
                ) {
                    onClick()
                }
            }


        }

    }
}

val basicSubscriptionInfoList = listOf(
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.prevent_opening_link_to_malicious_websites
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.available_on_all_your_devices
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.active_users_can_unlock_benefits
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.access_to_community
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_red_cross,
        CommonR.string.completely_ad_free
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_red_cross,
        CommonR.string.access_to_expert
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_red_cross,
        CommonR.string.email_projection
    )
)
val premiumSubscriptionInfoList = listOf(
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.all_features_from_basic
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.completely_ad_free
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.access_to_expert
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.email_projection
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_red_cross,
        CommonR.string.premium_for_six_people
    ),
)
val familySubscriptionInfoList = listOf(
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.premium_for_six_people
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.be_the_hero_of_your_family
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.completely_ad_free
    ),
)
val businessSubscriptionInfoList = listOf(
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.business_subscription_1
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.business_subscription_2
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.business_subscription_3
    ),
    SubscriptionInfoItem(
        DesignSystemR.drawable.ic_checked,
        CommonR.string.business_subscription_4
    ),

    )

data class SubscriptionInfoItem(
    @DrawableRes val drawableId: Int,
    @StringRes val infoStringResId: Int
)