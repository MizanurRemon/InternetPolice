package com.internetpolice.onboardings_presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView.SHOW_BUFFERING_WHEN_PLAYING
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews

@Composable
fun VideoOnBoardingScreen(navController: NavController, isFaq: Boolean, onButtonClick: () -> Unit) {
    val videoPairs = listOf(
        Pair(
            0,
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
        ),
        Pair(
            1,
            "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
        ),
        Pair(
            2,
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
        )
    )

    var visibleVideo1 by remember { mutableStateOf(false) }
    var visibleVideo2 by remember { mutableStateOf(false) }
    var visibleVideo3 by remember { mutableStateOf(false) }

    val playerOneProgress = remember {
        mutableStateOf(0f)
    }
    val playerTwoProgress = remember {
        mutableStateOf(0f)
    }
    val playerThreeProgress = remember {
        mutableStateOf(0f)
    }

    val showVideo = remember {
        mutableStateOf(false)
    }
    val showVideo1 = {
        visibleVideo1 = true
        visibleVideo2 = false
        visibleVideo3 = false
    }

    val showVideo2 = {
        visibleVideo1 = false
        visibleVideo2 = true
        visibleVideo3 = false
    }
    val showVideo3 = {
        visibleVideo1 = false
        visibleVideo2 = false
        visibleVideo3 = true
    }

    return Column(
        modifier = Modifier.background(AppBrush),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.h()))
            Row {
                val modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.w())
                IndicatorCompose(modifier = modifier.clickable {
                    showVideo1()
                }, progress = playerOneProgress.value)
                IndicatorCompose(modifier = modifier.clickable {
                    showVideo2()
                }, progress = playerTwoProgress.value)
                IndicatorCompose(modifier = modifier.clickable {
                    showVideo3()
                }, progress = playerThreeProgress.value)
            }

            val playerModifier = Modifier
                .padding(top = 20.h(), bottom = 20.h())
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .background(color = Color.White)


            if (visibleVideo1) {
                VideoView(
                    videoUrl = videoPairs[0].second,
                    modifier = playerModifier,
                    onVideoEnded = showVideo2,
                    onVideoPlaying = { durationComplete: Float ->
                        playerOneProgress.value = durationComplete
                    }
                )
            }
            if (visibleVideo2) {
                VideoView(
                    videoUrl = videoPairs[1].second,
                    modifier = playerModifier,
                    onVideoEnded = showVideo3,
                    onVideoPlaying = { durationComplete: Float ->
                        playerTwoProgress.value = durationComplete
                    }
                )
            }
            if (visibleVideo3)
                VideoView(
                    videoUrl = videoPairs[2].second,
                    modifier = playerModifier,
                    onVideoPlaying = { durationComplete: Float ->
                        playerThreeProgress.value = durationComplete
                    },
                    onVideoEnded = {

                    }
                )

        }
        if (!showVideo.value)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            ) {
                Image(
                    painter = painterResource(id = DesignSystemR.drawable.ic_play_video),
                    contentDescription = null,
                    modifier = Modifier
                        .size(115.r())
                        .background(color = Color.Transparent)
                        .align(Alignment.Center)
                        .noRippleClickable {
                            showVideo1()
                            showVideo.value = true
                        }
                )
            }


        AppActionButtonCompose(stringId = if (isFaq) CommonR.string.got_it else CommonR.string.continues,
            bgColor = ColorPrimaryDark,
            textColor = Color.White,
            modifier = Modifier.padding(start = 35.w(), end = 35.w()),
            onActionButtonClick = {

                onButtonClick()
            })
        Spacer(modifier = Modifier.height(height = 55.h()))
    }
}

@Composable
fun VideoView(
    videoUrl: String,
    modifier: Modifier,
    onVideoPlaying: (Float) -> Unit,
    onVideoEnded: () -> Unit,
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                val mediaItem = MediaItem.Builder()
                    .setUri(videoUrl)
                    .build()
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
                exoPlayer.addListener(
                    object : Player.Listener {
                        override fun onEvents(
                            player: Player,
                            events: Player.Events
                        ) {
                            super.onEvents(player, events)
                            onVideoPlaying(
                                (player.currentPosition.coerceAtLeast(0L)
                                    .toFloat() / player.duration.toFloat())
                            )
                        }

                        override fun onPlayerStateChanged(playWhenReady: Boolean, state: Int) {
                            if (state == ExoPlayer.STATE_ENDED) {
                                onVideoEnded()
                            }
                        }
                    }
                )
            }
    }
    DisposableEffect(key1 = AndroidView(
        modifier = modifier,
        factory = {
            StyledPlayerView(context).apply {
                player = exoPlayer
                useController = true
                setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING)
                setShutterBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        DesignSystemR.color.white
                    )
                )
            }
        }
    )) {
        onDispose {
            exoPlayer.release()
        }
    }

}

@Composable
@DevicePreviews
fun PreviewVideoOnBoardingScreen() {
    VideoOnBoardingScreen(rememberNavController(), isFaq = true, onButtonClick = {})
}

@Composable
fun IndicatorCompose(progress: Float = 0.0f, modifier: Modifier) {
    return LinearProgressIndicator(
        progress = progress,
        color = ColorTextPrimary,
        trackColor = ColorTextPrimary.copy(alpha = 0.25F),
        modifier = modifier
            .height(12.h())
            .clip(RoundedCornerShape(15.dp))
            .border(
                width = 0.dp,
                color = Color(0xFFE2E4EA),
                shape = RoundedCornerShape(15.dp)
            ),
    )
}