package com.internetpolice.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.internetpolice.core.designsystem.IpRanksType
import com.internetpolice.core.designsystem.R
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.ColorPrimaryDark
import com.internetpolice.core.designsystem.theme.ColorTextSecondary
import com.internetpolice.core.designsystem.theme.bodyMediumTextStyle
import com.internetpolice.core.designsystem.theme.heading1TextStyle

@Composable
fun LevelScoreCompose(
    score: Int,
    modifier: Modifier,
    showOnlyProgress: Boolean = false,
    strokeWidth: Double = 15.0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val ipRank = IpRanksType.fromScore(score)
    val percentage = (score.toFloat() / ipRank.maxLimit.toFloat())
    val currentPercentage = animateFloatAsState(
        targetValue =
        if (animationPlayed) percentage else 0f,
        animationSpec = tween(durationMillis = 4000)//, label = "label"
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = modifier
    ) {
        if (!showOnlyProgress)
            Image(
                painter = painterResource(id = R.drawable.ic_profile_bg),
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        CircularProgressIndicator(
            progress = 1f,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent),
            color = Color(0xffF0F5FF),
            strokeWidth = strokeWidth.r()
        )
        CircularProgressIndicator(
            progress = currentPercentage.value,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent),
            color = ColorPrimaryDark,
            strokeWidth = strokeWidth.r()
        )
        if (!showOnlyProgress)
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = com.internetpolice.core.common.R.string.level_up),
                    style = bodyMediumTextStyle.copy(ColorTextSecondary)
                )
                Text(
                    text = score.toString() + "/" + ipRank.maxLimit.toString(),
                    style = heading1TextStyle
                )

                Image(
                    painter = painterResource(id = ipRank.iconResId),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 30.h())
                        .fillMaxWidth()
                        .height(45.h())
                        .alpha(if (score < ipRank.maxLimit) 0.2f else 1f)
                )
                Text(
                    text = stringResource(id = ipRank.rankNameStringRes),
                    style = bodyMediumTextStyle.copy(ColorTextSecondary)
                )
            }
    }
}

@Preview
@Composable
fun PreviewLevelScoreCompose() {
    LevelScoreCompose(
        score = 240,
        modifier = Modifier,
        showOnlyProgress = false,
        strokeWidth = 15.0
    )
}
