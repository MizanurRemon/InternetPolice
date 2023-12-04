package com.internetpolice.app.home

import android.content.Context
import android.provider.Settings
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.internetpolice.app.permission.PermissionScreen
import com.internetpolice.app.waring.UrlBlockerService
import com.internetpolice.app.waring.isAccessibilitySettingsOn
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.COMMUNITY_URL
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.openExternalLink
import com.internetpolice.core.designsystem.*
import com.internetpolice.core.designsystem.components.*
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.report_domain.model.DomainModel
import com.internetpolice.report_presentation.utils.testImageUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onWebSearchResultClick: (DomainModel) -> Unit,
    onScreenWebsiteClick: () -> Unit,
    onReportClick: () -> Unit,
    onNewsItemClick: (Any) -> Unit,
    onMySubscriptionClick: () -> Unit,
    onSeeAllNewsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMyProfileClick: (Boolean) -> Unit,
    onAccountSettingsClick: () -> Unit,
    onAccountCreateClick: () -> Unit,
    onProfileEditClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showResult by remember {
        mutableStateOf(false)
    }

    val openInitialMessage = remember {
        mutableStateOf(false)
    }

    val openNewsDialog = remember {
        mutableStateOf(false)
    }
    val isVisited = remember { mutableStateOf(true) }
    val showWarning = remember {
        mutableStateOf(false)
    }


    ShowPopup(
        openDialog = openInitialMessage,
        titleResId = CommonR.string.initial_message_title,
        descriptionResId = CommonR.string.initial_message_details,
        dismissTextResId = CommonR.string.cancel,
        noAction = true,
        important = true
    ) {
        //Do stuff for deleting account
    }
    val context = LocalContext.current

    ShowNewsDialog(openNewsDialog = openNewsDialog)

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    navController.navigate(Route.AUTH_CHOOSE) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }

                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short,
                    )
                }

                is UiEvent.NavigateUp -> {
                    navController.navigateUp()
                }

                else -> {}
            }

        }
    }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                if ((!hasOverlayPermission(context) || !hasAccessibilityPermission(context))) {
                    isVisited.value = false
                }

            }

            else -> {
            }
        }
    }

    if (!isVisited.value) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(30.dp)
                    .clip(shape = RoundedCornerShape(40.dp))
            ) {


                PermissionScreen(
                    navController = navController,
                    onDoneClick = { isVisited.value = true }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 40.w(),
                            end = 40.w(),
                            top = 20.h()
                        ),
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(
                        painter = painterResource(id = DesignSystemR.drawable.ic_blue_cross),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {

                                if ((!hasOverlayPermission(context) || !hasAccessibilityPermission(
                                        context
                                    ))
                                ) showWarning.value = true else isVisited.value = true
                            },
                    )
                }
            }
        }
    }

    if (showWarning.value) {
        ShowPopUpWithImage(
            openDialog = showWarning,
            titleResId = CommonR.string.permission_not_granted,
            descriptionResId = CommonR.string.permission_not_granted_text,
            dismissTextResId = CommonR.string.go_back,
            confirmTextResId = CommonR.string.yes_i_am_sure,
            contentImage = DesignSystemR.drawable.ic_police_cross, sureClick = {
                showWarning.value = false
                isVisited.value = true
            }
        )
    }

    return ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HomeModalDrawerSheet(
                score = viewModel.state.userScore,
                isLoggedIn = viewModel.state.isLoggedIn,
                isProfileComplete = viewModel.state.isProfileComplete,
                userName = viewModel.state.userName,
                userImageUrl = viewModel.state.avatarUrl,
                scope = scope,
                drawerState = drawerState,
                onMySubscriptionClick = onMySubscriptionClick,
                onMyProfileClick = onMyProfileClick,
                onAccountSettingsClick = onAccountSettingsClick,
                onAccountCreateClick = onAccountCreateClick,
                onProfileEditClick = onProfileEditClick,
                onLogOutClick = {
                    viewModel.onEvent(HomeEvent.OnLogOut)
                }
            )
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = AppBrush)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.w(), top = 24.h(), end = 16.w())
                    .noRippleClickable {
                        showResult = false
                    }
            ) {
                TopBarCompose(
                    score = viewModel.state.userScore,
                    imageUrl = viewModel.state.avatarUrl,
                    onSearchTextChange = {
                        if (viewModel.state.isLoggedIn) {
                            viewModel.onEvent(HomeEvent.OnSearchResult(it))
                            showResult = true
                        }
                    },
                    isFocused = {
                        if (viewModel.state.isLoggedIn) {
                            showResult = it
                            if (showResult) {
                                viewModel.onEvent(HomeEvent.OnSearchResult(""))
                            }
                        }
                    },
                    onSettingsClick = onSettingsClick,
                    drawerState = drawerState,
                    scope = scope,
                    isGuest = !viewModel.state.isLoggedIn,
                    onProfileEditClick = onProfileEditClick
                )
                Spacer(modifier = Modifier.height(14.h()))

                Box(modifier = Modifier.fillMaxSize()) {

                    Column(modifier = Modifier.fillMaxSize()) {

                        CopActionComposeWithSearchResult(
                            name = viewModel.state.userName,
                            actionTitle = if (viewModel.state.isLoggedIn) {
                                if (viewModel.state.isProfileComplete) {
                                    stringResource(id = CommonR.string.check_your_progress)
                                } else {
                                    stringResource(id = CommonR.string.lets_complete_profile)
                                }
                            } else {
                                stringResource(id = CommonR.string.lets_create_account)
                            },
                            onActionButtonClick = {
                                if (viewModel.state.isLoggedIn) {
                                    if (viewModel.state.isProfileComplete) {
                                        navController.navigate(Route.PROFILE + "/false")
                                    } else {
                                        navController.navigate(Route.AVATAR_NAME_INPUT)
                                    }
                                } else {
                                    navController.navigate(Route.SIGN_UP)
                                }
                            },
                        )
                        Spacer(modifier = Modifier.height(18.h()))
                        HomeActionButtons(viewModel.state.isLoggedIn, onReportClick)
                        Spacer(modifier = Modifier.height(25.h()))
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 35.r(), topEnd = 35.r()),
                            shadowElevation = 8.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Column {
                                LatestNewsTopCompose(onSeeAllNewsClick)
                                Spacer(modifier = Modifier.height(8.h()))

                                if (viewModel.state.isShowDialog)
                                    CircularProgressIndicator(
                                        strokeWidth = 4.dp,
                                        color = ColorPrimaryDark,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(8.dp)
                                    )

                                val lazyColumnListState = rememberLazyListState()
                                LazyColumn(
                                    state = lazyColumnListState,
                                    modifier = Modifier.padding(bottom = 10.dp)
                                ) {
                                    items(viewModel.state.newsList) { news ->
                                        NewsItemCompose(
                                            imageUrl = news.image,
                                            title = news.title,
                                            description = news.description,
                                            tag = news.tag,
                                            dateText = news.date
                                        ) {
                                            onNewsItemClick(news.id)

                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.h()))
                            }
                        }

                        if (viewModel.state.isLogOutLoading)
                            LoadingDialog {}
                    }

                    if (showResult) {
                        Surface(
                            color = SEARCH_BOX_BG,
                            modifier = Modifier
                                .width(250.w())
                                .align(Alignment.TopCenter)
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(20.dp))
                        ) {
                            Column(
                                modifier =
                                Modifier.padding(vertical = 16.w())
                            ) {
                                Text(
                                    text = stringResource(id = CommonR.string.screen_websites),
                                    style = TextStyle(
                                        fontFamily = fontRoboto,
                                        fontSize = 18.ssp(),
                                        fontWeight = FontWeight.W500,
                                        lineHeight = 21.ssp(),
                                        textAlign = TextAlign.Left,
                                        color = SEARCH_BOX_HINT_COLOR
                                    ),
                                    modifier = Modifier
                                        .clickable {
                                            onScreenWebsiteClick()
                                        }
                                        .padding(horizontal = 16.w())
                                )
                                Spacer(modifier = Modifier.height(6.h()))
                                Spacer(
                                    modifier = Modifier
                                        .height(1.h())
                                        .fillMaxWidth()
                                        .background(color = ColorDivider)
                                )
                                Spacer(modifier = Modifier.height(6.h()))
                                repeat(viewModel.state.domainList.size) { index ->
                                    Text(
                                        text = viewModel.state.domainList[index].domain,
                                        style = bodyRegularTextStyle.copy(
                                            color = ColorTextPrimary,
                                            fontWeight = FontWeight.W500
                                        ),
                                        modifier = Modifier
                                            .padding(vertical = 5.h(), horizontal = 16.w())
                                            .clickable {
                                                onWebSearchResultClick(viewModel.state.domainList[index])
                                            }
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ShowNewsDialog(openNewsDialog: MutableState<Boolean>) {
    if (openNewsDialog.value)
        Dialog(
            onDismissRequest = { }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(32.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 20.h())
                            .wrapContentSize(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        AsyncImage(
                            model = testImageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.4f),
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(
                                id = DesignSystemR.drawable.ic_cancel
                            ),
                            modifier = Modifier
                                .padding(20.r())
                                .wrapContentSize()
                                .clickable {
                                    openNewsDialog.value = false
                                },
                            contentDescription = null
                        )
                    }

                    Text(
                        text =
                        stringResource(id = CommonR.string.news),
                        style = subHeading1TextStyle.copy(letterSpacing = 5.ssp())
                    )
                    Spacer(modifier = Modifier.height(20.h()))
                    Text(
                        text = "Scam warning",
                        style = heading1TextStyle.copy(color = ColorPrimaryDark)
                    )
                    Text(
                        "Lorem Ipsum is simply dummy text of the printing and typesetting " +
                                "industry. Lorem Ipsum has been the industry's standard.",
                        style = bodyRegularTextStyle,
                        modifier = Modifier.padding(vertical = 20.h(), horizontal = 40.w()),
                    )
                    Text("TUE, NOV 22, 2022", style = bodyXSRegularTextStyle)
                    Spacer(modifier = Modifier.weight(1f))
                    AppActionButtonCompose(
                        stringId = CommonR.string.learn_more,
                        modifier = Modifier
                            .padding(start = 20.w(), end = 20.w(), top = 10.h(), bottom = 20.h())
                            .fillMaxWidth()
                    ) {

                    }

                }


            }
        }
}


@Composable
private fun LatestNewsTopCompose(onSeeAllNewsClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable {
                onSeeAllNewsClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.h()))
        Image(
            painter = painterResource(id = DesignSystemR.drawable.ic_line),
            contentDescription = null,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.w(), end = 20.w(), top = 15.h()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = CommonR.string.latest_news),
                style = boldBodyTextStyle
            )
            Spacer(modifier = Modifier.weight(1f, fill = true))
            Text(
                text = stringResource(id = CommonR.string.see_all_news),
                style = bodyRegularTextStyle
            )
            Spacer(modifier = Modifier.width(8.w()))
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_arrow_right_dark),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(25.h()))
        Spacer(
            modifier = Modifier
                .height(1.h())
                .background(Color(0xffE5EAFF))
                .fillMaxSize()
        )

    }
}

@Composable
private fun CopActionComposeWithSearchResult(
    name: String,
    actionTitle: String,
    onActionButtonClick: () -> Unit
) {


    Box {
        CopActionCompose(
            title = stringResource(id = CommonR.string.agent_jim),
            actionTitle = actionTitle,
            bgId = DesignSystemR.drawable.ic_police_home,
            onActionButtonClick = onActionButtonClick,
            favIconUrl = ""
        )
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBarCompose(
    score: Int,
    imageUrl: String,
    onSearchTextChange: (String) -> Unit,
    isFocused: (Boolean) -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope,
    onSettingsClick: () -> Unit,
    onProfileEditClick: () -> Unit,
    isGuest: Boolean,
) {
    var searchTextFieldValue by remember {
        mutableStateOf(TextFieldValue(""))
    }
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = DesignSystemR.drawable.ic_settings_v2),
            contentDescription = null,
            modifier = Modifier
                .size(40.r())
                .noRippleClickable {
                    onSettingsClick()
                },
        )

        ConstraintLayout(
            modifier = Modifier
                .weight(1f)
                .background(color = Color.Transparent),
        ) {
            val (backgroundCompose, textInput) = createRefs()
            Box(
                modifier = Modifier
                    .padding(
                        start = 20.w(), end = 20.w(), top = 10.h(),
                        bottom = 10.h()
                    )
                    .height(40.h())
                    .width(250.w())
                    .fillMaxWidth()
                    .constrainAs(backgroundCompose) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .background(
                        color = ColorPrimaryLight,
                        shape = RoundedCornerShape(62.dp)
                    )
                    .clip(RoundedCornerShape(62.r())),
                contentAlignment = Alignment.CenterStart
            ) {

                Icon(
                    painter = painterResource(DesignSystemR.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 16.w()),
                    tint = SEARCH_BOX_HINT_COLOR
                )

            }
            TextField(
                value = searchTextFieldValue,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    //placeholderColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                onValueChange = {
                    onSearchTextChange(it.text)
                    searchTextFieldValue = it
                },
                modifier = Modifier
                    .padding(start = 60.w())
                    .onFocusChanged {
                        isFocused(it.isFocused)
                    }
                    .constrainAs(textInput) {
                        top.linkTo(backgroundCompose.top)
                        bottom.linkTo(backgroundCompose.bottom)
                        start.linkTo(backgroundCompose.start)
                        end.linkTo(backgroundCompose.end)
                    }
                    .clearFocusOnKeyboardDismiss()
                    .fillMaxWidth()
                    .background(color = Color.Transparent),
                placeholder = {
                    Text(
                        text = stringResource(id = CommonR.string.search_website),
                        style = TextStyle(
                            fontSize = 16.ssp(),
                            fontFamily = fontRoboto,
                            fontWeight = FontWeight.W500,
                            color = SEARCH_BOX_HINT_COLOR
                        ),
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = ColorTextPrimary,
                    fontSize = 16.ssp(),
                    fontFamily = fontRoboto,
                    fontWeight = FontWeight.W500,
                )
            )
        }


        ProfileImageCompose(
            isRankShow = true,
            score = score,
            profileImageUrl = imageUrl,
            enablePress = false,
            size = 40,
            modifier =
            Modifier
                .noRippleClickable {
                    scope.launch {
                        drawerState.open()
                    }
                },
            showLevelScoreCompose = !isGuest,
            isGuest = isGuest,
            onEditClick = onProfileEditClick
        )

    }
}

@Composable
private fun HomeActionButtons(isLoggedIn: Boolean, onReportClick: () -> Unit) {
    val context = LocalContext.current
    Row(modifier = Modifier.height(175.h())) {
        Card(
            colors = CardDefaults
                .elevatedCardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .weight(0.5.toFloat())
                .fillMaxSize()
                .clickable {
                    if (isLoggedIn)
                        onReportClick()
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = DesignSystemR.drawable.ic_megaphone),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp)
                )
                Spacer(modifier = Modifier.height(22.h()))
                Text(
                    text = stringResource(id = CommonR.string.report_website),
                    style = boldBodyTextStyle
                )
            }
        }
        Spacer(modifier = Modifier.width(12.w()))
        Card(
            colors = CardDefaults
                .elevatedCardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .weight(0.5.toFloat())
                .fillMaxSize()
                .clickable {
                    openExternalLink(COMMUNITY_URL, context)
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = DesignSystemR.drawable.ic_community),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp)
                )
                Spacer(modifier = Modifier.height(22.h()))
                Text(
                    text = stringResource(id = CommonR.string.visit_community),
                    style = boldBodyTextStyle
                )
            }
        }

    }
}

private fun hasOverlayPermission(context: Context): Boolean {
    return Settings.canDrawOverlays(context)
}

private fun hasAccessibilityPermission(context: Context): Boolean {
    return isAccessibilitySettingsOn(context, UrlBlockerService::class.java)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@DevicePreviews
fun PreviewTopBarCompose() {
    TopBarCompose(
        score = 30,
        imageUrl = "",
        onSearchTextChange = {
        },
        isFocused = {

        },
        drawerState = rememberDrawerState(DrawerValue.Closed),
        scope = rememberCoroutineScope(),
        onSettingsClick = {},
        isGuest = false,
        onProfileEditClick = {

        }
    )
}