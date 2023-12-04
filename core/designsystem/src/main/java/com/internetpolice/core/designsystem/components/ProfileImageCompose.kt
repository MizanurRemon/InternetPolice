package com.internetpolice.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.internetpolice.core.designsystem.IpRanksType
import com.internetpolice.core.designsystem.R
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.noRippleClickable
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews

@Composable
fun ProfileImageCompose(
    modifier: Modifier = Modifier,
    size: Int = 290,
    showEdit: Boolean = false,
    isGuest: Boolean = false,
    enablePress: Boolean = true,
    showLevelScoreCompose: Boolean = true,
    profileImageUrl: String = "",
    isRankShow: Boolean = true,
    score: Int,
    onEditClick: () -> Unit
) {
    val showLevelScore = remember {
        mutableStateOf(false)
    }

    val strokeWidth = (size / 19.34)
    val interactionSource = remember { MutableInteractionSource() }
    if (enablePress) {
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        showLevelScore.value = true
                    }

                    is PressInteraction.Release -> {
                        showLevelScore.value = false
                    }
                }
            }
        }
    }
    Box(
        modifier = modifier
            .size(size.r())
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {

            }
    ) {
        val imagePainter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = if (isGuest) "" else profileImageUrl)
                .apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                    error(
                        if (isGuest)
                            R.drawable.ic_guest else R.drawable.ic_default_avatar
                    )
                }).build()
        )
        Image(
            painter = painterResource(
                R.drawable.ic_avater_bg_without_stroke
            ),
            contentDescription = "",
            modifier = modifier.fillMaxSize(),
        )
        if (!showLevelScore.value) {
            Image(
                painter = imagePainter,
                contentDescription = "",
                modifier = modifier.fillMaxSize(),
            )
            if (showLevelScoreCompose)
                LevelScoreCompose(
                    score,
                    modifier = modifier.fillMaxSize(),
                    showOnlyProgress = true,
                    strokeWidth = strokeWidth
                )
            if (!isGuest && isRankShow)
                Image(
                    painter = painterResource(
                        IpRanksType.fromScore(score).iconResId
                    ),
                    contentDescription = null,
                    modifier = modifier
                        .width((size / 2).w())
                        .height((size / 6.30).h())
                        .align(Alignment.BottomCenter)
                )
            if (showEdit)
                Image(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null,
                    modifier = modifier
                        .padding(bottom = (size / 5.8).h(), end = 0.h())
                        .size((size / 4).r())
                        .align(Alignment.BottomEnd)
                        .noRippleClickable {
                            onEditClick()
                        }
                )
        }
        if (showLevelScore.value) {
            LevelScoreCompose(
                score,
                modifier = modifier.fillMaxSize(),
                strokeWidth = strokeWidth
            )
        }
    }
}


@Composable
@DevicePreviews
fun PreviewProfileImageCompose() {
    ProfileImageCompose(
        score = 30,
        profileImageUrl = "https://avatars.githubusercontent.com/u/19679715?v=4",
        enablePress = false,
        showLevelScoreCompose = false,
        onEditClick = {

        }
    )
}
