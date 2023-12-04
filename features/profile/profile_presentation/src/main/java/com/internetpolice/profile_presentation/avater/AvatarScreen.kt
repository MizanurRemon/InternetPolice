import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.core.designsystem.components.ProfileImageCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.profile_presentation.avater.*
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.ScreenshotState
import com.smarttoolfactory.screenshot.rememberScreenshotState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR


@Composable
fun AvatarScreen(
    snackbarHostState: SnackbarHostState,
    name: String,
    onBackClick: () -> Unit,
    onGoToProfile: () -> Unit,
    viewModel: AvatarViewModel = hiltViewModel(),
) {
    val state = viewModel.state

    val scope = rememberCoroutineScope()

    val selectedSkinColorIndex = remember {
        mutableStateOf(-1)
    }
    val selectedHairColorIndex = remember {
        mutableStateOf(-1)
    }
    val selectedAvatarData = remember {
        mutableStateOf(AvatarData())
    }
    val selectedHairStyleIndex = remember {
        mutableStateOf(-1)
    }
    val selectedFacePatternStyleIndex = remember {
        mutableStateOf(-1)
    }
    val selectedGenderIndex = remember {
        mutableStateOf(-1)
    }
    val selectedTabIndex = remember {
        mutableStateOf(0)
    }
    val isFullyCompleted = remember {
        mutableStateOf(false)
    }
    val showCompleteProfileDialog = remember {
        mutableStateOf(false)
    }
    val screenshotState = rememberScreenshotState()
    val context = LocalContext.current

    if (state.userEntity?.avatarImagePath != null && !isFullyCompleted.value) {
        selectedSkinColorIndex.value =
            getEnumIndex(SkinBrushType::class.java, state.userEntity?.color)
        selectedHairColorIndex.value =
            getEnumIndex(HairColorType::class.java, state.userEntity?.hairColor)

        selectedHairStyleIndex.value =
            getEnumIndex(HairStyleType::class.java, state.userEntity?.hairStyle)

        selectedFacePatternStyleIndex.value =
            getEnumIndex(FaceType::class.java, state.userEntity?.faceStyle)

        selectedGenderIndex.value =
            getEnumIndex(GenderType::class.java, state.userEntity?.gender)

        isFullyCompleted.value = true

        selectedAvatarData.value = AvatarData(
            skinTone = getEnumByName<SkinBrushType>(state.userEntity?.color ?: ""),
            face = getEnumByName<FaceType>(state.userEntity?.faceStyle ?: ""),
            hairStyleType = getEnumByName<HairStyleType>(state.userEntity?.hairStyle ?: ""),
            hairColorType = getEnumByName<HairColorType>(state.userEntity?.hairColor ?: ""),
            genderType = getEnumByName<GenderType>(state.userEntity?.gender ?: "")
        )

    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->
            when (event) {
                is UiEvent.Success -> {
                    scope.launch {
                        delay(100)
                        showCompleteProfileDialog.value = true
                    }
                }

                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short,
                    )
                }

                is UiEvent.NavigateUp -> {
                }

                else -> {}
            }
        }
    }
    val tabs = remember {
        mutableStateListOf(
            Triple(CommonR.string.color, AvatarTabState.INCOMPLETE, AvatarTabState.SELECTED),
            Triple(CommonR.string.face, AvatarTabState.INCOMPLETE, AvatarTabState.UNSELECTED),
            Triple(CommonR.string.hairStyle, AvatarTabState.INCOMPLETE, AvatarTabState.UNSELECTED),
            Triple(CommonR.string.gender, AvatarTabState.INCOMPLETE, AvatarTabState.UNSELECTED),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppToolbarCompose(
                title = stringResource(id = CommonR.string.home),
                modifier = Modifier.padding(start = 20.w(), end = 20.w(), top = 30.h()),
            ) {
                onBackClick()
            }
            AvatarPreviewCompose(
                selectedAvatarData,
                screenshotState,
                selectedSkinColorIndex,
                selectedHairColorIndex,
                selectedHairStyleIndex,
                selectedFacePatternStyleIndex,
                showHairColorIndicator =
                selectedTabIndex.value == 0,
            )
            Text(text = name, style = heading1TextStyle, modifier = Modifier)
            ControllerCompose(
                selectedAvatarData,
                tabs,
                selectedTabIndex,
                selectedSkinColorIndex,
                selectedHairColorIndex,
                selectedHairStyleIndex,
                selectedFacePatternStyleIndex,
                selectedGenderIndex,
                isFullyCompleted
            ) {
                viewModel.onEvent(AvatarEvent.OnSubmitAvatar(selectedAvatarData.value))
            }
        }
        if (showCompleteProfileDialog.value) {
            BackHandler { showCompleteProfileDialog.value = false }
            AvatarCompletePopup(
                url = viewModel.state.url,
                name = viewModel.state.name,
                score = viewModel.state.totalPoints,
                showCompleteProfileDialog,
                onGoToProfile
            )
        }
        if (viewModel.state.isShowLoading)
            LoadingDialog {}

        BackHandler {

        }
    }
}

@Composable
private fun ControllerCompose(
    selectedAvatarData: MutableState<AvatarData>,
    tabs: SnapshotStateList<Triple<Int, AvatarTabState, AvatarTabState>>,
    selectedTabIndex: MutableState<Int>,
    selectedSkinColorIndex: MutableState<Int>,
    selectedHairColorIndex: MutableState<Int>,
    selectedHairStyleIndex: MutableState<Int>,
    selectedFacePatternStyleIndex: MutableState<Int>,
    selectedGenderIndex: MutableState<Int>,
    isFullyCompleted: MutableState<Boolean>,
    onFinish: (AvatarData) -> Unit,
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(topStart = 35.r(), topEnd = 35.r()),
        shadowElevation = 8.r(),
        modifier = Modifier
            .padding(top = 20.h())
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AvatarTabsCompose(tabs, selectedTabIndex, isFullyCompleted)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.h())
                    .background(color = Color(0xffD6D6D6))
            )
            OnColorSelection(
                selectedTabIndex,
                selectedSkinColorIndex,
                selectedHairColorIndex,
                tabs
            )
            OnFacePatternSelection(
                selectedTabIndex,
                selectedFacePatternStyleIndex,
                tabs
            )
            OnHairStyleSelection(
                selectedTabIndex,
                selectedHairStyleIndex,
                tabs
            )
            OnGenderSelection(selectedTabIndex, selectedGenderIndex, tabs)

            ColorTabCompose(
                selectedTabIndex,
                selectedSkinColorIndex,
                selectedHairColorIndex,
                tabs,
                isFullyCompleted
            )
            FacePatternTabCompose(
                selectedTabIndex,
                selectedFacePatternStyleIndex,
                tabs,
                isFullyCompleted
            )
            HairStyleTabCompose(
                selectedTabIndex,
                selectedHairStyleIndex,
                tabs,
                isFullyCompleted
            )
            GenderTabCompose(
                selectedTabIndex,
                selectedGenderIndex,
                tabs,
                isFullyCompleted
            ) {
                onFinish(selectedAvatarData.value.apply {
                    it@ selectedAvatarData.value.genderType = it
                    it@ selectedAvatarData.value.hairColorType =HairColorType.values()[selectedHairColorIndex.value]
                    it@ selectedAvatarData.value.skinTone =SkinBrushType.values()[selectedSkinColorIndex.value]
                    it@ selectedAvatarData.value.face =FaceType.values()[selectedFacePatternStyleIndex.value]
                    it@ selectedAvatarData.value.hairStyleType =HairStyleType.values()[selectedHairStyleIndex.value]
                })
            }
            Spacer(modifier = Modifier.height(16.h()))

        }


    }
}

@Composable
fun AvatarPreviewCompose(
    selectedAvatarData: MutableState<AvatarData>,
    screenshotState: ScreenshotState,
    selectedSkinColorIndex: MutableState<Int>,
    selectedHairColorIndex: MutableState<Int>,
    selectedHairStyleIndex: MutableState<Int>,
    selectedFacePatternIndex: MutableState<Int>,
    showHairColorIndicator: Boolean,
) {
    ScreenshotBox(
        screenshotState = screenshotState,
        modifier = Modifier
            .padding(top = 30.h())
            .size(290.r())
            .clip(CircleShape)
            .background(
                shape = CircleShape,
                color = Color.Transparent
            ),
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            //Background
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_avater_bg),
                contentDescription = null,
                Modifier.fillMaxSize()
            )
            //skin color
            if (selectedSkinColorIndex.value >= 0) {
                Image(
                    painter = painterResource(id = AvatarFaceColorResIdList[selectedSkinColorIndex.value].second),
                    contentDescription = null,
                    Modifier.size(250.r())
                )
                selectedAvatarData.value.apply {
                    skinTone = AvatarFaceColorResIdList[selectedSkinColorIndex.value].first
                }
            }
            //skin color

            //Body
            Image(
                painter = painterResource(id = DesignSystemR.drawable.ic_avater_body),
                contentDescription = null,
                modifier = Modifier
                    .padding(24.r())
                    .size(250.r())
            )
            //Body


            //Hair color indicator
            if (showHairColorIndicator && selectedHairColorIndex.value >= 0) {
                ColorItemCompose(
                    brush = HairColorBrushList[selectedHairColorIndex.value].second,
                    modifier = Modifier
                        .padding(56.r())
                        .size(28.r())
                        .clip(RoundedCornerShape(5.r()))
                        .align(alignment = Alignment.TopEnd)
                )
                selectedAvatarData.value.apply {
                    hairColorType = HairColorBrushList[selectedHairColorIndex.value].first
                }
            }
            //Hair color indicator


            //face pattern
            if (selectedFacePatternIndex.value >= 0) {
                Image(
                    painter = painterResource(
                        id = AvatarFacePatternResIdList[selectedFacePatternIndex.value].second
                    ),
                    contentDescription = null,
                    Modifier.size(250.r())
                )
                selectedAvatarData.value.apply {
                    face = AvatarFacePatternResIdList[selectedFacePatternIndex.value].first
                }
            }
            //face pattern


            //Hair style
            if (selectedHairStyleIndex.value >= 0 &&
                selectedHairColorIndex.value >= 0
            ) {
                Icon(
                    painter = painterResource(
                        id = AvatarHairStyleMainResIdList[selectedHairStyleIndex.value].second
                    ), contentDescription = null,
                    modifier = Modifier.size(250.r()),
                    tint = HairColorList[selectedHairColorIndex.value].second
                )
                selectedAvatarData.value.apply {
                    hairStyleType = AvatarHairStyleMainResIdList[selectedHairStyleIndex.value].first
                }
            }
            //Hair style

        }
    }
}

@Composable
private fun OnColorSelection(
    selectedTabIndex: MutableState<Int>,
    selectedSkinColorIndex: MutableState<Int>,
    selectedHairColorIndex: MutableState<Int>,
    tabs: SnapshotStateList<Triple<Int, AvatarTabState, AvatarTabState>>,
) {

    if (selectedTabIndex.value == 0) {
        val result = tabs[0]
        tabs[0] = Triple(
            result.first,
            AvatarTabState.INCOMPLETE,
            AvatarTabState.SELECTED
        )
    }
    if (selectedSkinColorIndex.value >= 0 &&
        selectedHairColorIndex.value >= 0
    ) {
        val result = tabs[0]
        tabs[0] = Triple(
            result.first,
            AvatarTabState.COMPLETED,
            result.third
        )
    } else {
        val result = tabs[0]
        tabs[0] = Triple(
            result.first,
            AvatarTabState.INCOMPLETE,
            result.third
        )
    }
}


@Composable
private fun OnHairStyleSelection(
    selectedTabIndex: MutableState<Int>,
    selectedHairStyleIndex: MutableState<Int>,
    tabs: SnapshotStateList<Triple<Int, AvatarTabState, AvatarTabState>>,
) {

    if (selectedTabIndex.value == 2) {
        val result = tabs[2]
        tabs[2] = Triple(
            result.first,
            AvatarTabState.INCOMPLETE,
            AvatarTabState.SELECTED
        )
    }
    if (selectedHairStyleIndex.value >= 0
    ) {
        val result = tabs[2]
        tabs[2] = Triple(
            result.first,
            AvatarTabState.COMPLETED,
            result.third
        )
    } else {
        val result = tabs[2]
        tabs[2] = Triple(
            result.first,
            AvatarTabState.INCOMPLETE,
            result.third
        )
    }
}

@Composable
private fun OnFacePatternSelection(
    selectedTabIndex: MutableState<Int>,
    selectedFacePatternStyleIndex: MutableState<Int>,
    tabs: SnapshotStateList<Triple<Int, AvatarTabState, AvatarTabState>>,
) {
    if (selectedTabIndex.value == 1) {
        val result = tabs[1]
        tabs[1] = Triple(
            result.first,
            AvatarTabState.INCOMPLETE,
            AvatarTabState.SELECTED
        )
    }
    if (selectedFacePatternStyleIndex.value >= 0
    ) {
        val result = tabs[1]
        tabs[1] = Triple(
            result.first,
            AvatarTabState.COMPLETED,
            result.third
        )
    } else {
        val result = tabs[1]
        tabs[1] = Triple(
            result.first,
            AvatarTabState.INCOMPLETE,
            result.third
        )
    }
}

@Composable
private fun OnGenderSelection(
    selectedTabIndex: MutableState<Int>,
    selectedGenderIndex: MutableState<Int>,
    tabs: SnapshotStateList<Triple<Int, AvatarTabState, AvatarTabState>>,
) {
    if (selectedTabIndex.value == 3) {
        val result = tabs[3]
        tabs[3] = Triple(
            result.first,
            AvatarTabState.INCOMPLETE,
            AvatarTabState.SELECTED
        )
    }
    if (selectedGenderIndex.value >= 0
    ) {
        val result = tabs[3]
        tabs[3] = Triple(
            result.first,
            AvatarTabState.COMPLETED,
            result.third
        )
    } else {
        val result = tabs[3]
        tabs[3] = Triple(
            result.first,
            AvatarTabState.INCOMPLETE,
            result.third
        )
    }
}


@Composable
private fun ColorTabCompose(
    selectedTabIndex: MutableState<Int>,
    selectedSkinColorIndex: MutableState<Int>,
    selectedHairColorIndex: MutableState<Int>,
    tabs: SnapshotStateList<Triple<Int, AvatarTabState, AvatarTabState>>,
    isFullyCompleted: MutableState<Boolean>,
) {
    if (tabs[0].third == AvatarTabState.SELECTED) {
        Column(
            modifier =
            Modifier
                .background(color = Color.White)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 30.h(), start = 20.w(), end = 20.w())
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = CommonR.string.skin), style = bodyMediumTextStyle)
                Spacer(modifier = Modifier.weight(1f))
                ColorSelectionCompose(
                    index = 0,
                    selectedSkinColorIndex,
                    SkinBrush.map { it.second })
                ColorSelectionCompose(
                    index = 1,
                    selectedSkinColorIndex,
                    SkinBrush.map { it.second })
                ColorSelectionCompose(
                    index = 2,
                    selectedSkinColorIndex,
                    SkinBrush.map { it.second })
                ColorSelectionCompose(
                    index = 3,
                    selectedSkinColorIndex,
                    SkinBrush.map { it.second })
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = CommonR.string.hair), style = bodyMediumTextStyle)
                Spacer(modifier = Modifier.weight(1f))

                ColorSelectionCompose(
                    index = 0, selectedHairColorIndex,
                    HairColorBrushList.map { it.second }
                )
                ColorSelectionCompose(
                    index = 1, selectedHairColorIndex,
                    HairColorBrushList.map { it.second }
                )
                ColorSelectionCompose(
                    index = 2, selectedHairColorIndex,
                    HairColorBrushList.map { it.second }
                )
                ColorSelectionCompose(
                    index = 3, selectedHairColorIndex,
                    HairColorBrushList.map { it.second }
                )

            }
            Spacer(modifier = Modifier.weight(1f))
            AppActionButtonCompose(
                stringId = CommonR.string.next,
                bgColor = if (tabs[0].second == AvatarTabState.COMPLETED)
                    ColorPrimaryDark else ColorPrimaryDark.copy(alpha = 0.35f),
                enable = tabs[0].second == AvatarTabState.COMPLETED,
            ) {
                onTabItemClick(selectedTabIndex, 1, tabs, isFullyCompleted)
            }
            Spacer(modifier = Modifier.height(55.h()))

        }
    }
}

@Composable
private fun HairStyleTabCompose(
    selectedTabIndex: MutableState<Int>,
    selectedHairStyleIndex: MutableState<Int>,
    tabs: SnapshotStateList<Triple<Int, AvatarTabState, AvatarTabState>>,
    isFullyCompleted: MutableState<Boolean>,
) {
    if (tabs[2].third == AvatarTabState.SELECTED) {
        Column(
            modifier =
            Modifier
                .background(color = Color.White)
                .fillMaxSize()
                .padding(top = 20.h(), start = 20.w(), end = 20.w())
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                Modifier.height(200.h())
            ) {
                items(AvatarHairStyleResIdList.size) { index ->
                    HairStyleSelectionCompose(
                        index = index,
                        selectedHairStyleIndex,
                        AvatarHairStyleResIdList.map { it.second }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            AppActionButtonCompose(
                stringId = CommonR.string.next,
                bgColor = if (tabs[2].second == AvatarTabState.COMPLETED)
                    ColorPrimaryDark else ColorPrimaryDark.copy(alpha = 0.35f),
                enable = tabs[2].second == AvatarTabState.COMPLETED,
            ) {
                //Go to next tab so setting next index
                onTabItemClick(selectedTabIndex, 3, tabs, isFullyCompleted)
            }
            Spacer(modifier = Modifier.height(55.h()))
        }
    }
}

@Composable
private fun FacePatternTabCompose(
    selectedTabIndex: MutableState<Int>,
    selectedFacePatternStyleIndex: MutableState<Int>,
    tabs: SnapshotStateList<Triple<Int, AvatarTabState, AvatarTabState>>,
    isFullyCompleted: MutableState<Boolean>,
) {
    if (tabs[1].third == AvatarTabState.SELECTED) {
        Column(
            modifier =
            Modifier
                .background(color = Color.White)
                .fillMaxSize()
                .padding(top = 20.h(), start = 20.w(), end = 20.w())
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                Modifier.height(200.r())
            ) {
                items(AvatarFacePatternResIdList.size) { index ->
                    FacePatternSelectionCompose(
                        index = index,
                        selectedFacePatternStyleIndex,
                        AvatarFacePatternSelectionResIdList.map { it.second }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            AppActionButtonCompose(
                stringId = CommonR.string.next,
                bgColor = if (tabs[1].second == AvatarTabState.COMPLETED)
                    ColorPrimaryDark else ColorPrimaryDark.copy(alpha = 0.35f),
                enable = tabs[1].second == AvatarTabState.COMPLETED,
            ) {
                //Go to next tab so setting next index
                onTabItemClick(selectedTabIndex, 2, tabs, isFullyCompleted)
            }
            Spacer(modifier = Modifier.height(55.h()))

        }
    }
}

@Composable
private fun GenderTabCompose(
    selectedTabIndex: MutableState<Int>,
    selectedGenderIndex: MutableState<Int>,
    tabs: SnapshotStateList<Triple<Int, AvatarTabState, AvatarTabState>>,
    isFullyCompleted: MutableState<Boolean>,
    onDone: (GenderType) -> Unit,
) {
    if (tabs[3].third == AvatarTabState.SELECTED) {
        Column(
            modifier =
            Modifier
                .background(color = Color.White)
                .fillMaxSize()
                .padding(top = 20.h(), start = 20.w(), end = 20.w())
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                Modifier.height(100.w())
            ) {
                items(AvatarGenderStringResIdList.size) { index ->
                    GenderSelectionCompose(
                        index = index,
                        selectedGenderIndex,
                        AvatarGenderStringResIdList.map { it.second }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            AppActionButtonCompose(
                stringId = CommonR.string.done,
                bgColor = if (tabs[3].second == AvatarTabState.COMPLETED)
                    ColorPrimaryDark else ColorPrimaryDark.copy(alpha = 0.35f),
                enable = tabs[3].second == AvatarTabState.COMPLETED,
            ) {
                onDone(AvatarGenderStringResIdList[selectedGenderIndex.value].first)
                onTabItemClick(selectedTabIndex, 3, tabs, isFullyCompleted)
            }
            Spacer(modifier = Modifier.height(55.h()))

        }
    }
}


@Composable
private fun ColorSelectionCompose(
    index: Int,
    selectedIndex: MutableState<Int>,
    brushList: List<Brush>,
) {
    AvatarSelectionItemCompose(
        isSelected = selectedIndex.value == index
    ) {
        ColorItemCompose(brush = brushList[index], modifier = Modifier) {
            if (selectedIndex.value == index) {
                selectedIndex.value = -1
            } else
                selectedIndex.value = index
        }
    }
}

@Composable
private fun FacePatternSelectionCompose(
    index: Int,
    selectedIndex: MutableState<Int>,
    drawableResIdList: List<Int>,
) {
    AvatarSelectionItemCompose(
        isSelected = selectedIndex.value == index,
        size = 70
    ) {
        FacePatternItemCompose(drawableResId = drawableResIdList[index], modifier = Modifier) {
            if (selectedIndex.value == index) {
                selectedIndex.value = -1
            } else
                selectedIndex.value = index
        }
    }
}

@Composable
private fun GenderSelectionCompose(
    index: Int,
    selectedIndex: MutableState<Int>,
    titleStringResIdList: List<Int>,
) {
    AvatarSelectionItemCompose(
        isSelected = selectedIndex.value == index,
        size = 70
    ) {
        GenderItemCompose(
            titleStringResId = titleStringResIdList[index],
            isSelected = selectedIndex.value == index,
        ) {
            if (selectedIndex.value == index) {
                selectedIndex.value = -1
            } else
                selectedIndex.value = index
        }
    }
}


@Composable
private fun HairStyleSelectionCompose(
    index: Int,
    selectedIndex: MutableState<Int>,
    drawableResIdList: List<Int>,
) {
    AvatarSelectionItemCompose(
        isSelected = selectedIndex.value == index,
        size = 70
    ) {
        HairStyleItemCompose(drawableResId = drawableResIdList[index], modifier = Modifier) {
            if (selectedIndex.value == index) {
                selectedIndex.value = -1
            } else
                selectedIndex.value = index
        }
    }
}

@Composable
private fun HairStyleItemCompose(
    @DrawableRes drawableResId: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Image(
        painterResource(id = drawableResId),
        contentDescription = null,
        modifier = modifier
            .padding(10.w())
            .fillMaxSize()
            .noRippleClickable { onClick?.let { it() } }
    )
}

@Composable
private fun FacePatternItemCompose(
    @DrawableRes drawableResId: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Image(
        painterResource(id = drawableResId),
        contentDescription = null,
        modifier = modifier
            .padding(10.w())
            .fillMaxSize()
            .noRippleClickable { onClick?.let { it() } }
    )
}

@Composable
private fun GenderItemCompose(
    @StringRes titleStringResId: Int,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Text(
        stringResource(id = titleStringResId),
        modifier = modifier
            .fillMaxSize()
            .background(
                color = if (isSelected)
                    Color(0xffF6FCFC)
                else Color.Transparent
            )
            .padding(start = 8.w(), end = 8.w(), top = 24.h(), bottom = 20.h())
            .noRippleClickable { onClick?.let { it() } },
        style = bodyMediumTextStyle,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ColorItemCompose(
    brush: Brush,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = brush)
            .noRippleClickable { onClick?.let { it() } }
    )
}

@Composable
private fun AvatarSelectionItemCompose(
    isSelected: Boolean = false,
    size: Int = 50,
    compose: @Composable () -> Unit,
) {
    Box(modifier = Modifier.wrapContentSize()) {
        Box(
            modifier = Modifier
                .padding(horizontal = 10.w(), vertical = 15.w())
                .size(size.r())
                .border(
                    width = 4.w(),
                    color = if (isSelected) ColorTextPrimary else Color(0xffEEF4FF),
                    shape = RoundedCornerShape(10.r())
                )
                .clip(RoundedCornerShape(10.r()))
        ) {
            compose()
        }
        if (isSelected)
            Image(
                painter =
                painterResource(id = DesignSystemR.drawable.ic_avatar_component_selected),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 4.w(), bottom = 10.h())
                    .align(alignment = Alignment.BottomEnd)
            )
    }
}

@Composable
@DevicePreviews
private fun PreviewColorTabCompose() {
    val selectedSkinColorIndex = remember {
        mutableStateOf(-1)
    }
    val selectedHairColorIndex = remember {
        mutableStateOf(-1)
    }
    val tabs = remember {
        mutableStateListOf(
            Triple(CommonR.string.color, AvatarTabState.INCOMPLETE, AvatarTabState.SELECTED),
            Triple(CommonR.string.face, AvatarTabState.INCOMPLETE, AvatarTabState.UNSELECTED),
            Triple(CommonR.string.hairStyle, AvatarTabState.INCOMPLETE, AvatarTabState.UNSELECTED),
            Triple(CommonR.string.gender, AvatarTabState.INCOMPLETE, AvatarTabState.UNSELECTED),
        )
    }
    val selectedTabIndex = remember {
        mutableStateOf(0)
    }
    val isFullyCompleted = remember {
        mutableStateOf(false)
    }

    ColorTabCompose(
        selectedTabIndex,
        selectedSkinColorIndex,
        selectedHairColorIndex,
        tabs,
        isFullyCompleted
    )
}

@Composable
private fun AvatarTabsCompose(
    tabs: SnapshotStateList<Triple<Int, AvatarTabState, AvatarTabState>>,
    selectedTabIndex: MutableState<Int>,
    isFullyCompleted: MutableState<Boolean>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(18.w()))
        tabs.forEachIndexed { indexTab, pairTab ->
            AvatarTabItem(
                name = stringResource(id = pairTab.first),
                state = pairTab,
            ) {
                if (tabs[if (indexTab > 0) indexTab - 1 else 0].second
                    == AvatarTabState.COMPLETED
                ) {
                    onTabItemClick(selectedTabIndex, indexTab, tabs, isFullyCompleted)
                }
            }
        }
        Spacer(modifier = Modifier.width(18.w()))
    }
}

private fun onTabItemClick(
    selectedTabIndex: MutableState<Int>,
    indexTab: Int,
    tabs: SnapshotStateList<Triple<Int, AvatarTabState, AvatarTabState>>,
    isFullyCompleted: MutableState<Boolean>,
) {
    selectedTabIndex.value = indexTab
    repeat(tabs.size, fun(index: Int) {
        val result = tabs[index]
        tabs[index] = Triple(
            result.first,
            result.second,
            if (indexTab == index) {
                AvatarTabState.SELECTED
            } else AvatarTabState.UNSELECTED,
        )
    })
    isFullyCompleted.value = tabs.none { it.second != AvatarTabState.COMPLETED }
}

enum class AvatarTabState {
    COMPLETED,
    SELECTED,
    UNSELECTED,
    INCOMPLETE
}

@Composable
fun AvatarTabItem(
    name: String,
    state: Triple<Int, AvatarTabState, AvatarTabState>,
    onClick: ((Triple<Int, AvatarTabState, AvatarTabState>) -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.w(), vertical = 15.w())
            .height(30.h())
            .clip(RoundedCornerShape(4.r()))
            .background(
                color =
                if (state.second == AvatarTabState.COMPLETED &&
                    state.third == AvatarTabState.SELECTED
                ) {
                    ColorPrimaryDark
                } else if (state.second == AvatarTabState.COMPLETED ||
                    state.third == AvatarTabState.UNSELECTED
                ) {
                    Color(0xffEEF4FF)
                } else {
                    ColorPrimaryDark
                }
            )
            .noRippleClickable {
                if (onClick != null) {
                    onClick(state)
                }
            }
    ) {
        Text(
            text = name, style = bodyBoldTextStyle.copy(
                color =
                if (state.third == AvatarTabState.SELECTED
                ) {
                    Color.White
                } else if (state.second == AvatarTabState.COMPLETED) {
                    Color(0xff3E6FCC)
                } else {
                    Color(0x803E6FCC)
                },
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(vertical = 2.h(), horizontal = 10.w())
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AvatarCompletePopup(
    url: String,
    name: String,
    score: Int,
    showCompleteProfileDialog: MutableState<Boolean>,
    onGoToProfile: () -> Unit,
) {
    Popup(
        properties = PopupProperties(
            dismissOnBackPress = true, usePlatformDefaultWidth = false
        ),
        onDismissRequest = {
            showCompleteProfileDialog.value = !showCompleteProfileDialog.value
        }) {
        AvatarCompleteContent(url, name, score, showCompleteProfileDialog, onGoToProfile)
    }
}

@Composable
private fun AvatarCompleteContent(
    url: String,
    name: String,
    score: Int,
    showCompleteProfileDialog: MutableState<Boolean>,
    onGoToProfile: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.9f)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(100.h()))
            ProfileImageCompose(
                score = score,
                profileImageUrl = url,
                isRankShow = false, onEditClick = {

                }
            )
            Spacer(modifier = Modifier.height(60.h()))
            Text(
                text = stringResource(id = CommonR.string.congratulations) + " $name!",
                style = heading1TextStyle.copy(
                    color = Color(0xffADBCF3),
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(40.h()))
            Text(
                text = stringResource(id = CommonR.string.you_have_earn_10_points),
                style = subHeading1TextStyle.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(20.h()))

            Text(
                text = stringResource(id = CommonR.string.profile_complete_message),
                style = bodyRegularTextStyle.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            AppActionButtonCompose(
                stringId = CommonR.string.go_to_profile,
                modifier = Modifier.padding(horizontal = 36.w())
            ) {
                showCompleteProfileDialog.value = false
                onGoToProfile()
            }
            Spacer(modifier = Modifier.height(55.h()))
        }

    }
}


@Composable
@DevicePreviews
fun PreviewAvatarCompleteDialogContent() {
    val showCompleteProfileDialog = remember {
        mutableStateOf(true)
    }
    AvatarCompletePopup(
        url = "https://bucket-ip-website.s3.eu-central-1.amazonaws.com/avatars/light_face1_ponytail_lightred.png",
        name = "Sultan.",
        score = 10,
        showCompleteProfileDialog,
        onGoToProfile = {
        }
    )
}

@Composable
@DevicePreviews
fun PreviewAvatarCompletePopupContent() {
    val showCompleteProfileDialog = remember {
        mutableStateOf(true)
    }
    LocalInspectionMode.provides(true)
    AvatarCompleteContent(
        url = "https://bucket-ip-website.s3.eu-central-1.amazonaws.com/avatars/light_face1_ponytail_lightred.png",
        name = "Sultan.",
        score = 10,
        showCompleteProfileDialog,
        onGoToProfile = {
        })
}



