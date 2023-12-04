package com.internetpolice.app.waring

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.internetpolice.core.common.R
import com.internetpolice.core.common.util.FAV_ICON_PREFIX_URL
import com.internetpolice.core.common.util.intent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.languageList
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.AppCancelButtonCompose
import com.internetpolice.core.designsystem.theme.ColorPrimaryDark
import com.internetpolice.core.designsystem.theme.ColorTextFieldPlaceholder
import com.internetpolice.core.designsystem.theme.ColorTextPrimary
import com.internetpolice.core.designsystem.theme.ColorTextSecondary
import com.internetpolice.core.designsystem.theme.LowRedBrush
import com.internetpolice.core.designsystem.theme.MediumOrangeBrush
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.bodyXSBoldTextStyle
import com.internetpolice.core.designsystem.theme.fontRoboto
import com.internetpolice.core.designsystem.theme.heading1TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.network.dto.DomainDetailsDto
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.report_presentation.ScoreType
import com.internetpolice.report_presentation.getScoreType
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun WarningScreen(
    domainDetailsDto: DomainDetailsDto,
    onBack: () -> Unit,
    onGoBackSafely: () -> Unit,
    websiteDetails: () -> Unit,
) {

    val contentString = buildAnnotatedString {

        withStyle(
            style = SpanStyle(
                fontSize = 16.ssp(),
                color = ColorTextSecondary,
            )
        ) {
            append(
                stringResource(
                    when (getScoreType(domainDetailsDto.trustScore)) {
                        ScoreType.Medium -> R.string.suspicious_info
                        ScoreType.Low -> R.string.malicious_info
                        else -> R.string.suspicious_info
                    }
                ) + " "
            )
        }

        pushStringAnnotation(
            tag = CommonR.string.learn_more.toString(),
            annotation = CommonR.string.learn_more.toString()
        )

        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
                fontFamily = fontRoboto,
                fontSize = 16.ssp(),
                color = ColorTextSecondary
            )
        ) {
            append(stringResource(id = CommonR.string.learn_more).replace(".", ""))
        }

    }

    val context = LocalContext.current
    val tag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
    return Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .padding(start = 18.w(), end = 18.w(), top = 30.h())
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppToolbarCompose(
                title = stringResource(id = CommonR.string.home),
                onBackClick = { onBack() }
            )
            Spacer(modifier = Modifier.height(30.h()))
            Image(
                painter = if (tag == languageList[1].tag) {
                    painterResource(id = DesignSystemR.drawable.ic_logo_horizontal_dutch)
                } else {
                    painterResource(id = DesignSystemR.drawable.ic_logo_horizontal)
                },
                contentDescription = null,
                modifier = Modifier
                    .height(60.h())
                    .fillMaxWidth()
                    .background(Color.Transparent)
            )
            Spacer(modifier = Modifier.height(16.h()))
            Image(
                painter = painterResource(
                    id =
                    when (getScoreType(domainDetailsDto.trustScore)) {
                        ScoreType.Medium -> DesignSystemR.drawable.ic_warining_cop_suspicious
                        ScoreType.Low -> DesignSystemR.drawable.ic_waring_cop_malicious
                        else -> DesignSystemR.drawable.ic_warining_cop_suspicious
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.h())
                    .background(Color.Transparent)
            )
            Spacer(modifier = Modifier.height(36.h()))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = "$FAV_ICON_PREFIX_URL${domainDetailsDto.domain}")
                            .apply(block = fun ImageRequest.Builder.() {
                                error(com.internetpolice.core.designsystem.R.drawable.earthicon)
                            }).build()
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.r())
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = domainDetailsDto.domain ?: "",
                    color = ColorTextPrimary,
                    fontWeight = FontWeight.W500,
                    fontSize = 16.sp,
                    lineHeight = 25.sp,
                )
            }
            Spacer(modifier = Modifier.height(10.h()))
            Text(
                text = stringResource(
                    id =
                    when (getScoreType(domainDetailsDto.trustScore)) {
                        ScoreType.Medium -> CommonR.string.is_suspicious
                        ScoreType.Low -> CommonR.string.is_malicious
                        else -> CommonR.string.is_suspicious
                    }
                ),
                style = heading1TextStyle.copy(color = ColorPrimaryDark)
            )
            Spacer(modifier = Modifier.height(22.h()))

            ClickableText(text = contentString,
                modifier = Modifier.padding(vertical = 10.h()),
                onClick = { offset ->
                    contentString.getStringAnnotations(
                        tag = CommonR.string.learn_more.toString(),
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        websiteDetails()
                    }
                }
            )

            Spacer(modifier = Modifier.height(22.h()))
            Box(
                modifier = Modifier.padding(start = 20.w(), end = 20.w()),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .height(35.h())
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = ColorTextFieldPlaceholder,
                            shape = RoundedCornerShape(20.dp),
                        )
                        .background(
                            brush = when (getScoreType(domainDetailsDto.trustScore)) {
                                ScoreType.Low -> LowRedBrush
                                ScoreType.Medium -> MediumOrangeBrush
                                else -> MediumOrangeBrush
                            }
                        ),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = stringResource(id = CommonR.string.trust_score) + " ${domainDetailsDto.trustScore}/100",
                        style = bodyXSBoldTextStyle.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Image(
                        painter = painterResource(
                            id = DesignSystemR.drawable.ic_info
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 6.w())
                    )
                }
                Image(
                    painter = painterResource(
                        id =
                        when (getScoreType(domainDetailsDto.trustScore)) {
                            ScoreType.Medium -> DesignSystemR.drawable.ic_rating_medium
                            ScoreType.Low -> DesignSystemR.drawable.ic_rating_low
                            else -> DesignSystemR.drawable.ic_rating_medium
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(50.r())
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            AppActionButtonCompose(
                stringId = CommonR.string.go_back_safely,
                modifier = Modifier.padding(start = 18.w(), end = 18.w())
            ) {
                onGoBackSafely()
            }
            Spacer(modifier = Modifier.height(12.h()))
            AppCancelButtonCompose(
                titleStringResId = CommonR.string.continue_anyway, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.w(), end = 18.w())
            ) {

                UrlBlockerService.trustedUrls.add(domainDetailsDto)
                context.startActivity(
                    intent(
                        domainDetailsDto.browserPackageName!!,
                        domainDetailsDto.capturedUrl!!
                    )
                )

            }
            Spacer(modifier = Modifier.height(55.h()))

        }
    }

}

@Composable
@DevicePreviews
fun PreviewWarningScreen() {
    WarningScreen(
        domainDetailsDto = DomainDetailsDto(),
        onBack = {
        },
        onGoBackSafely = { },
        websiteDetails = {}
    )
}