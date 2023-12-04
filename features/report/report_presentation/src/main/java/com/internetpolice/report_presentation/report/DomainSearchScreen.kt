package com.internetpolice.report_presentation.report

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.report_presentation.ReportEvent
import com.internetpolice.report_presentation.ReportViewModel
import com.internetpolice.report_presentation.ScoreCompose
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.internetpolice.core.common.R as CommonR

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DomainSearchScreen(
    viewModel: ReportViewModel,
    onBack: () -> Unit,
    isAnyItemSelected: (Boolean) -> Unit,
) {
    var state = viewModel.state
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    return Box(
        modifier = Modifier
            .background(brush = AppBrush)
            .padding(top = 30.h(), start = 16.w(), end = 16.w())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppToolbarCompose(
                title = stringResource(id = CommonR.string.home), onBackClick = onBack
            )
            Spacer(modifier = Modifier.height(100.h()))
            Text(
                text = stringResource(id = CommonR.string.what_is_name_website),
                style = heading1TextStyle
            )
            Spacer(modifier = Modifier.height(20.h()))
            Text(
                text = stringResource(id = CommonR.string.enter_name_of_website),
                style = bodyRegularTextStyle,
            )
            Spacer(modifier = Modifier.height(40.h()))
            TextField(
                value = state.searchText,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboardController?.hide()
                    viewModel.onEvent(ReportEvent.OnSearchResult)
                    defaultKeyboardAction(ImeAction.Search)
                }),
                onValueChange = {
                    viewModel.onEvent(ReportEvent.OnSearchChange(it))
                    scope.launch {
                        delay(1000)
                        viewModel.onEvent(ReportEvent.OnSearchResult)
                    }

                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(start = 16.w(), end = 16.w())
                    .clip(RoundedCornerShape(15.dp))
                    .border(
                        width = 1.dp, color = Color(0xFFE2E4EA), shape = RoundedCornerShape(15.dp)
                    )
                    .onFocusChanged {

                    },
                textStyle = bodyRegularTextStyle.copy(
                    color = ColorTextFieldPlaceholder, textAlign = TextAlign.Left
                ),
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = ColorTextFieldPlaceholder
                    )
                },
            )

            Spacer(modifier = Modifier.height(24.dp))

            val onSelectedUid: (Int) -> Unit = {
                isAnyItemSelected(it!=-1)
            }

            if (state.isSearching)
                CircularProgressIndicator(
                    strokeWidth = 4.dp,
                    color = ColorPrimaryDark,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                )
            val lazyColumnListState = rememberLazyListState()
            LazyColumn(
                state = lazyColumnListState, modifier = Modifier,
                contentPadding = PaddingValues(bottom = 160.h())
            ) {
                items(state.domainList) {
                    ScoreCompose(
                        uid = it.id,
                        websiteName = it.domain,
                        url = it.domain,
                        score = it.trustScore,
                        selectedUid = state.selectedDomainId,
                        onSelectedUid = onSelectedUid
                    ) { _ ->
                        viewModel.onEvent(ReportEvent.OnSelectedDomain(it))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
@DevicePreviews
fun previewWebSearchScreen() {
//    WebSearchScreen(onBack = {}, isAnyItemSelected = {}, onWebsiteScoreClick = {})
}