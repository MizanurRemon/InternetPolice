package com.internetpolice.report_presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun ScoreCompose(
    uid: Int,
    websiteName: String, url: String, score: Double,
    voteCount: Int = 0, negativeResult: Int = 0,
    selectedUid: Int = 0,
    onSelectedUid: (Int) -> Unit,
    onClick: (Any) -> Unit,
) {
    var isExtended by remember { mutableStateOf(false) }
    val scoreType = getScoreType(score)
    var isSelected = selectedUid == uid
    return Card(
        colors = CardDefaults
            .elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 6.h(), bottom = 6.h(), start = 16.w(), end = 16.w())
            .fillMaxWidth()
            .noRippleClickable {
                isSelected = !isSelected
                if (isSelected)
                    onSelectedUid(uid)
                else onSelectedUid(0)
                onClick(Any())
            },
        border = if (isSelected)
            BorderStroke(width = 2.r(), color = ColorPrimaryDark)
        else null
    ) {

        Row(
            modifier = Modifier
                .padding(16.r())
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(
                    id =
                    when (scoreType) {
                        ScoreType.High -> DesignSystemR.drawable.ic_rating_high
                        ScoreType.Medium -> DesignSystemR.drawable.ic_rating_medium
                        else -> DesignSystemR.drawable.ic_rating_low
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(33.r())
            )
            Text(
                text = websiteName,
                style = bodyBoldTextStyle,
                modifier = Modifier
                    .padding(start = 10.w(), end = 5.w()),
                  //  .fillMaxWidth(0.5f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Image(
                painter = painterResource(
                    id = if (isExtended) DesignSystemR.drawable.ic_collapse
                    else DesignSystemR.drawable.ic_extend
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 5.w())
                    .size(18.r())
                    .clickable {
                        isExtended = !isExtended;
                    }
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
                    text = "${scoreType.name} $score/100",
                    style = bodyXSBoldTextStyle.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W500,
                        fontSize = 14.ssp()
                    ),
                    modifier = Modifier.padding(horizontal = 10.w())
                )
            }
        }
        Row(modifier = Modifier.padding(start = 16.w(), end = 16.w(), bottom = 10.h())) {
            Text(
                text = negativeResult.toString() + " " + stringResource(id = CommonR.string.negative_result),
                style = bodyXSRegularTextStyle.copy(color = Color.Black)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = voteCount.toString() + " " + stringResource(id = CommonR.string.reports),
                style = bodyXSRegularTextStyle.copy(color = Color.Black)
            )
        }
        if (isExtended) {
            Text(
                text = url,
                style = TextStyle(
                    fontFamily = fontRoboto,
                    fontSize = 16.ssp(),
                    fontWeight = FontWeight.W500,
                    letterSpacing = 0.sp,
                    color = ColorTextPrimary
                ),
                modifier = Modifier.padding(start = 16.w(), end = 16.w(), bottom = 16.h()),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

enum class ScoreType {
    High, Medium, Low
}

@Composable
@DevicePreviews
fun PreviewScoreCompose() {
    ScoreCompose(
        uid = 1,
        websiteName = "facebook.group.privacy.admin.password.internet.police.european.policy",
        url = "www.facebook.com",
        score = 90.0,
        voteCount = 10, negativeResult = 2,
        selectedUid = 12,
        onSelectedUid = {

        }) {

    }
}

fun getScoreType(score: Double): ScoreType {
    return if (score > 70.0) {
        ScoreType.High
    } else if (score in 30.0..70.0) {
        ScoreType.Medium
    } else {
        ScoreType.Low
    }
}