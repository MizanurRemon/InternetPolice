package com.internetpolice.news_presentation.NewsDetails

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.internetpolice.core.common.util.capitalizeFirstCharacter
import com.internetpolice.core.common.util.convertHtmlToText
import com.internetpolice.core.common.util.shareStringWithOthers
import com.internetpolice.news_presentation.News.TabItem
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR


@OptIn(ExperimentalGlideComposeApi::class)
@Composable

fun NewsDetailsScreen(onBack: () -> Unit, viewModel: NewsDetailsViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(brush = AppBrush)
    ) {
        Column {
            AppToolbarCompose(
                title = stringResource(id = CommonR.string.home),
                modifier = Modifier.padding(horizontal = 20.w(), vertical = 30.h())
            ) {
                onBack()
            }

            if (!viewModel.state.isShowDialog) {

                Box {
                    GlideImage(
                        model = viewModel.state.image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((configuration.screenHeightDp / 3).h()),
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painter = painterResource(id = DesignSystemR.drawable.ic_share),
                        contentDescription = null,
                        modifier = Modifier
                            .align(alignment = Alignment.BottomEnd)
                            .padding(32.r())
                            .clickable {
                                shareStringWithOthers(viewModel.state.newsLink, context)
                            }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                        .padding(vertical = 10.h(), horizontal = 32.w())
                ) {
                    TabItem(viewModel.state.tag.capitalizeFirstCharacter())
                    Spacer(modifier = Modifier.height(22.dp))
                    Text(
                        text = convertHtmlToText(viewModel.state.title),
                        style = heading1TextStyle.copy(textAlign = TextAlign.Left)
                    )
                    Row(modifier = Modifier.padding(top = 18.h(), bottom = 32.h())) {
                        Text(
                            text = viewModel.state.date,
                            style = bodyXSRegularTextStyle.copy(color = ColorTextPrimary)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = viewModel.state.author.capitalizeFirstCharacter(),
                            style = bodyXSRegularTextStyle.copy(color = ColorTextPrimary)
                        )
                    }

                    HtmlToTextWithImage(text = viewModel.state.content)
                }
            } else {
                LoadingDialog {

                }
            }


        }

    }


}

@Composable
@DevicePreviews
fun PreviewNewsDetailsScreen() {
    NewsDetailsScreen(onBack = {

    })
}


