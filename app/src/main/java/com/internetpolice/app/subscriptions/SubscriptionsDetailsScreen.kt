package com.internetpolice.app.subscriptions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.components.InfoItemCompose
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.w

@Composable
fun MySubscriptionDetailsScreen(onBack: () -> Unit, onSubscriptionCancelled: () -> Unit) {
    val openCancelSubscriptionDialog = remember {
        mutableStateOf(false)
    }
    ShowPopup(
        openDialog = openCancelSubscriptionDialog,
        titleResId = CommonR.string.cancel_subscription,
        descriptionResId = CommonR.string.cancel_subscription_info,
        dismissTextResId = CommonR.string.no,
        confirmTextResId = CommonR.string.yes,
        important = true
    ) {
        onSubscriptionCancelled()
    }

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .padding(20.r())
    ) {
        Spacer(modifier = Modifier.height(30.r()))
        AppToolbarCompose(title = stringResource(id = CommonR.string.home)) {
            onBack()
        }
        Spacer(modifier = Modifier.height(12.h()))
        Card(
            modifier = Modifier.wrapContentHeight(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults
                .elevatedCardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(modifier = Modifier.padding(bottom = 32.h())) {
                Row(
                    modifier =
                    Modifier
                        .padding(
                            top = 24.h(),
                            start = 32.w(),
                            end = 32.w(),
                            bottom = 14.h()
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = DesignSystemR.drawable.ic_my_subscription),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.r())
                    )
                    Spacer(modifier = Modifier.width(22.w()))
                    Text(text = "Family", style = subHeading1TextStyle)
                    Spacer(modifier = Modifier.weight(1f))
                    AppCancelButtonCompose(
                        titleStringResId = CommonR.string.cancel_subscription,
                        modifier = Modifier.height(45.h()),
                        fontSize = 10.ssp(),
                        lineHeight = 23.ssp(),
                        radius = 100.dp
                    ) {
                        openCancelSubscriptionDialog.value = true
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.h())
                        .background(color = ColorDivider)
                )
                Text(
                    text = "You subscription will automatically renew on 01/01/2023." +
                            " You’ll be charged €4,99 / month.",
                    style = bodyRegularTextStyle.copy(color = Color(0xff999999)),
                    modifier = Modifier.padding(
                        start = 32.w(),
                        top = 16.h(),
                        end = 32.w()
                    )
                )
                Text(
                    text = stringResource(id = CommonR.string.subscription_includes),
                    style = bodyMediumTextStyle,
                    modifier = Modifier.padding(
                        start = 32.w(),
                        top = 24.h(),
                        end = 32.w(),
                        bottom = 2.h()
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.w())
                        .height(1.h())
                        .background(color = ColorDivider)
                )
                InfoItemCompose(
                    titleStringResId = CommonR.string.subscription_benefit_1,
                    iconResId = DesignSystemR.drawable.ic_checked
                )
                Spacer(modifier = Modifier.height(16.h()))
                InfoItemCompose(
                    titleStringResId = CommonR.string.subscription_benefit_2,
                    iconResId = DesignSystemR.drawable.ic_red_cross
                )
                Spacer(modifier = Modifier.height(24.h()))
                AppActionButtonCompose(
                    stringId = CommonR.string.manage_subscription,
                    modifier = Modifier.padding(horizontal = 32.w())
                ) {

                }
                Text(
                    text = stringResource(id = CommonR.string.subscription_members),
                    style = bodyMediumTextStyle,
                    modifier = Modifier.padding(
                        start = 32.w(),
                        top = 24.h(),
                        end = 32.w(),
                        bottom = 2.h()
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.w())
                        .height(1.h())
                        .background(color = ColorDivider)
                )
                Spacer(modifier = Modifier.height(24.h()))
                SubscriberItemCompose(
                    imageUrl = "https://picsum.photos/200",
                    name = "John Doe (you)",
                    role = "Subscription Manager",
                    isCurrentUser = true
                )

            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SubscriberItemCompose(
    imageUrl: String,
    name: String,
    isCurrentUser: Boolean = false,
    role: String
) {
    return Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.w(), end = 32.w()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.size(57.r()),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 27.w()),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = name, style = bodyRegularTextStyle)
            Text(text = role, style = bodyXSRegularTextStyle)
        }
        Spacer(modifier = Modifier.width(32.w()))
        if (isCurrentUser)
            Image(
                painter =
                painterResource(id = DesignSystemR.drawable.ic_checked),
                contentDescription = null
            )
    }
}