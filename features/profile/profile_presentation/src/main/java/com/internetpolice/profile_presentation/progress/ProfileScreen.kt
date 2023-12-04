package com.internetpolice.profile_presentation.progress

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.FAV_ICON_PREFIX_URL
import com.internetpolice.core.common.util.SpecialTagsFromServer
import com.internetpolice.core.designsystem.IpReportStatusType
import com.internetpolice.core.designsystem.R
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.components.LevelScoreCompose
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.core.designsystem.components.ProfileImageCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.profile_domain.model.VoteModel
import java.util.*
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun ProfileScreen(
    navController: NavHostController,
    isGuest: Boolean = true,
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: () -> Unit,
    goToAvatar: (String) -> Unit,
    onInfoClick: () -> Unit
) {
    val isBottomModalOpen = remember {
        mutableStateOf(false)
    }
    val showPointDialog = remember {
        mutableStateOf(false)
    }

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
                title = stringResource(
                    id = if (isBottomModalOpen.value) CommonR.string.profile
                    else CommonR.string.home
                ), modifier = Modifier.padding(
                    start = 20.w(),
                    top = 30.h(),
                    end = 20.w(),
                    bottom = 50.h()
                )
            ) {
                onBack()
            }
            ProfileImageCompose(
                modifier = Modifier
                    .scale(.9f),
                score = viewModel.state.userScore,
                profileImageUrl = viewModel.state.url,
                showEdit = true,
                isGuest = isGuest, onEditClick = {
                    goToAvatar(viewModel.state.name)
                }
            )
            NameWithRankCompose(isGuest, viewModel.state.name)
            ProfileDataCompose(viewModel, showPointDialog, isGuest, navController, onInfoClick = {
                onInfoClick()
            })
        }
        if (showPointDialog.value)
            PointDialogContent(viewModel.state.selectedPoint, viewModel.state.name) {
                showPointDialog.value = !showPointDialog.value
            }
        if (viewModel.state.isShowDialog)
            LoadingDialog {}
    }

    BackHandler {
        onBack()
    }
}

@Composable
@DevicePreviews
fun PreviewGuestProfileScreen() {
    //ProfileScreen(isGuest = true,  onBack = {})
}

@Composable
@DevicePreviews
fun PreviewLoggedInUserProfileScreen() {
    // ProfileScreen(isGuest = false, onBack = {})
}

@Composable
private fun ProfileDataCompose(
    viewModel: ProfileViewModel,
    showPointDialog: MutableState<Boolean>,
    isGuest: Boolean,
    navController: NavHostController,
    onInfoClick: () -> Unit,
) {

    return Surface(
        color = Color.White,
        shape = RoundedCornerShape(topStart = 35.r(), topEnd = 35.r()),
        shadowElevation = 8.dp,
        modifier = Modifier
            .padding(top = 30.h())
            .fillMaxSize()
            .height((LocalConfiguration.current.screenHeightDp - 10).dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(
                modifier = Modifier
                    .padding(top = 20.h())
                    .height(3.h())
                    .width(35.w())
                    .background(Color(0xffE5EAFF))
                    .fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .padding(top = 6.h())
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabItemCompose(
                    isSelected = viewModel.state.isPointSelected.value,
                    titleStringResId = CommonR.string.points,
                    viewModel.state.userScore
                ) {
                    viewModel.state.isPointSelected.value = !viewModel.state.isPointSelected.value
                    viewModel.state.isReportSelected.value = !viewModel.state.isReportSelected.value
                }
                TabItemCompose(
                    isSelected = viewModel.state.isReportSelected.value,
                    titleStringResId = CommonR.string.reports,
                    viewModel.state.votes.size

                ) {
                    viewModel.state.isPointSelected.value = !viewModel.state.isPointSelected.value
                    viewModel.state.isReportSelected.value = !viewModel.state.isReportSelected.value
                }
            }
            if (viewModel.state.isPointSelected.value) {
                if (isGuest) {
                    Column(
                        modifier =
                        Modifier.padding(vertical = 32.h(), horizontal = 24.w())
                    ) {
                        Text(
                            text =
                            stringResource(id = CommonR.string.complete_your_profile_first_for_points),
                            style = bodyRegularTextStyle
                                .copy(color = Color(0xffC4C4C4))
                        )
                        AppActionButtonCompose(
                            stringId = CommonR.string.complete_profile,
                            modifier = Modifier.padding(bottom = 55.h(), top = 24.h())
                        ) {

                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(top = 8.h())
                    ) {
                        items(viewModel.state.points.size) {
                            ProfilePointItemCompose(
                                viewModel.state.points[it].point,
                                viewModel.state.points[it].rewardCode,
                                onInfoClick = {
                                    onInfoClick()
                                }, onItemClick = {
                                    viewModel.onEvent(ProfileEvent.OnSelectedPoint(viewModel.state.points[it].point))
                                    showPointDialog.value = true
                                }
                            )
                        }
                    }
                }
            }

            if (viewModel.state.isReportSelected.value)
                LazyColumn(
                    modifier = Modifier.padding(top = 8.h())
                ) {
                    items(viewModel.state.votes.size) {
                        ProfileReportItemCompose(
                            viewModel.state.votes[it],
                            onClick = {
                                navController.navigate(Route.REPORT_DETAILS + "/${viewModel.state.votes[it]}")
                            })
                    }
                }

            Spacer(modifier = Modifier.height(8.h()))
        }
    }
}

@Composable
fun TabItemCompose(
    isSelected: Boolean,
    @StringRes titleStringResId: Int,
    count: Int,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.noRippleClickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = titleStringResId),
            style = bodyMediumTextStyle.copy(
                color =
                if (isSelected)
                    Color(0xFF3468C8) else
                    Color(0xff8D8D8D)
            ),
            modifier = Modifier
                .padding(bottom = 3.h())
                .wrapContentSize()
        )
        Text(
            text = count.toString(),
            style = subHeading1TextStyle.copy(
                color =
                if (isSelected) ColorTextPrimary else Color(0xff5B5B5B)
            ),
            modifier = Modifier.padding(
                bottom = 4.h()
            )
        )
        Spacer(
            modifier = Modifier
                .height(if (isSelected) 4.h() else 2.h())
                .width((LocalConfiguration.current.screenWidthDp / 2.0).dp)
                .background(
                    color =
                    if (isSelected) Color(0xff005DFF) else Color(0xffE8E8E8)
                )
        )

    }
}

@Composable
private fun NameWithRankCompose(isGuest: Boolean, name: String) {
    Column(
        modifier = Modifier.padding(top = 10.h()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = name, style = heading1TextStyle)
        Spacer(modifier = Modifier.height(20.h()))
        Text(
            text =
            if (isGuest) stringResource(id = CommonR.string.no_ranking)
            else stringResource(id = CommonR.string.internet_police_officer),
            style = bodyRegularTextStyle
        )
    }
}


@Composable
fun ProfilePointItemCompose(
    score: Int,
    text: String,
    onItemClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    return Card(
        modifier = Modifier
            .padding(horizontal = 16.w(), vertical = 8.h())
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onItemClick()
            },
        colors = CardDefaults
            .elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.r()),
        shape = RoundedCornerShape(16),
    ) {
        Row(
            modifier = Modifier
                .padding(16.r()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_xp_point_orange),
                contentDescription = null,
                modifier = Modifier.size(54.r())
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.w(), end = 8.w())
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = CommonR.string.received) + " $score " + stringResource(
                            id = CommonR.string.points
                        ).lowercase(Locale.ROOT) + "!",
                        style = bodyBoldTextStyle.copy(fontSize = 18.sp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = DesignSystemR.drawable.ic_info),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onInfoClick()
                        }
                    )
                }
                Text(
                    text = stringResource(id = SpecialTagsFromServer.valueOf(text).value),
                    style = bodyXSRegularTextStyle.copy(color = ColorTextSecondary)
                )
            }

        }

    }
}

@Composable
fun ProfileReportItemCompose(
    voteModel: VoteModel,
    onClick: () -> Unit,
) {
    return Card(
        modifier = Modifier
            .padding(horizontal = 16.w(), vertical = 8.h())
            .fillMaxWidth()
            .wrapContentHeight()
            .noRippleClickable { onClick() },
        colors = CardDefaults
            .elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.r()),
        shape = RoundedCornerShape(16),
    ) {
        Row(
            modifier = Modifier.padding(16.r()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(54.r()),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = "${FAV_ICON_PREFIX_URL}${voteModel.domainName}")
                        .apply(block = fun ImageRequest.Builder.() {
                            error(R.drawable.ic_report_item)
                        }).build()
                ),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.w(), end = 8.w())
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = voteModel.domainName,
                        style = bodyBoldTextStyle
                    )
                    Spacer(modifier = Modifier.width(10.w()))
                    Image(
                        painter =
                        painterResource(
                            id = IpReportStatusType.valueOf(voteModel.voteStatus).iconResId
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.h()))
                Text(
                    text = voteModel.createdDate + " - " +
                            stringResource(id = CommonR.string.reported_as) +
                            " " + voteModel.voteType.lowercase(),
                    style = bodyXSRegularTextStyle
                )
            }
        }

    }
}


@Composable
@DevicePreviews
fun PreviewProfilePointItemCompose() {
    // ProfilePointItemCompose(onClick = {})
}

@Composable
@DevicePreviews
fun PreviewProfileReportItemCompose() {
//    ProfileReportItemCompose(
//        isApproved = true,
//        date = "29 jan,2022",
//        reportType = stringResource(id = CommonR.string.suspicious),
//        onClick = {})
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PointDialogContent(score: Int, name: String, onDismiss: () -> Unit) {
    return Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp)
                .background(color = Color.Black.copy(alpha = 0.8f)),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LevelScoreCompose(
                    score, modifier = Modifier
                        .padding(top = 80.h(), bottom = 30.h())
                        .size(280.r())
                )
                Spacer(modifier = Modifier.height(30.h()))
                Text(
                    text = stringResource(id = CommonR.string.congratulations) + " $name!",
                    style = heading1TextStyle.copy(
                        color = Color(0xffADBCF3),
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(40.h()))
                Text(
                    text = stringResource(id = CommonR.string.you_have_earn) + " $score " + stringResource(
                        id = CommonR.string.points
                    ) + "!",
                    style = subHeading1TextStyle.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(20.h()))

                Text(
                    text = stringResource(id = CommonR.string.profile_points_dialog_message),
                    style = bodyRegularTextStyle.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                AppActionButtonCompose(
                    stringId = CommonR.string.great,
                    modifier = Modifier.padding(horizontal = 36.w())
                ) {
                    onDismiss()
                }
                Spacer(modifier = Modifier.height(55.h()))
            }

        }
    }
}


@Composable
@DevicePreviews
fun PreviewProfileScreen() {
    //ProfileScreen(score = 120, onBack = {})
}

@Composable
@DevicePreviews
fun PreviewPointDialogContent() {
    PointDialogContent(score = 10, name = "", onDismiss = {})
}


