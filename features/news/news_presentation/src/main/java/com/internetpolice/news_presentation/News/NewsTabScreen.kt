package com.internetpolice.news_presentation.News

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.internetpolice.core.common.util.capitalizeFirstCharacter
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.components.NewsItemCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import java.util.Locale

@OptIn(ExperimentalUnitApi::class)
@Composable

fun NewsTabScreen(
    onBack: () -> Unit,
    onNewsItemClick: (Any) -> Unit,
    viewModel: NewsViewModel = hiltViewModel(),
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppToolbarCompose(
                title = stringResource(id = CommonR.string.home),
                modifier = Modifier.padding(top = 30.h(), start = 20.w(), end = 20.w())
            ) {
                onBack()
            }
            Spacer(modifier = Modifier.height(25.h()))
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 35.r(), topEnd = 35.r()),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(12.h()))
                    Image(
                        painter = painterResource(id = DesignSystemR.drawable.ic_line),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.height(16.h()))
                    Text(
                        text = stringResource(id = CommonR.string.news).uppercase(Locale.ROOT),
                        style = boldBodyTextStyle.copy(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        letterSpacing = TextUnit(2F, TextUnitType.Sp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        viewModel.state.tabItemsNames.forEachIndexed { index, name ->
                            TabItem(
                                name = name.capitalizeFirstCharacter(),
                                isSelected = viewModel.state.tabbedItemIndex.value == index
                            ) {
                                viewModel.state.tabbedItemIndex.value  = index
                                viewModel.onEvent(NewsEvent.OnTabClick(name))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(25.h()))

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
                        modifier = Modifier.fillMaxSize()
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
                    Spacer(
                        modifier = Modifier
                            .height(1.h())
                            .background(Color(0xffE5EAFF))
                            .fillMaxSize()
                    )
                }


            }
        }
    }

}

@Composable
@DevicePreviews
fun PreviewNewsTabScreen() {
    NewsTabScreen(onBack = {

    }, onNewsItemClick = {

    })
}

@Composable
fun TabItem(name: String, isSelected: Boolean = false, onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                color =
                if (isSelected) ColorPrimaryDark else Color(0xffEEF4FF)
            )
            .noRippleClickable {
                if (onClick != null) {
                    onClick()
                }
            }
    ) {
        Text(
            text = name, style = bodyXXSBoldTextStyle.copy(
                color =
                if (isSelected) Color.White else Color(0xff3E6FCC)
            ),
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 30.dp)
        )
    }
}