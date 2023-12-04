package com.internetpolice.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.internetpolice.core.common.util.capitalizeFirstCharacter
import com.internetpolice.core.common.util.convertHtmlToText
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItemCompose(
    imageUrl: String,
    title: String,
    description: String,
    tag: String, dateText: String, onClick: () -> Unit
) {

    val context = LocalContext.current

    return Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .noRippleClickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 16.dp)
                .clip(RoundedCornerShape(7.dp))
        ) {
            GlideImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(107.w())
                    .height(90.h()),
                contentScale = ContentScale.FillBounds
            )
        }
        Column {
            Text(
                text = convertHtmlToText(title),
                style = bodyXSBoldTextStyle,
                modifier = Modifier.padding(end = 5.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = convertHtmlToText(description),
                modifier = Modifier.padding(end = 20.dp),
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                style = bodyXSRegularTextStyle
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = tag.capitalizeFirstCharacter(), style = bodyXXSBoldTextStyle,
                    modifier = Modifier
                        .background(
                            color = Color(0xffEEF4FF),
                            shape = RoundedCornerShape(3.5.dp)
                        )
                        .padding(start = 5.w(), end = 5.w(), top = 2.h(), bottom = 2.h()),
                )
                Spacer(modifier = Modifier.width(8.w()))
                Text(text = dateText, style = bodyXXSRegularTextStyle)
            }
        }


    }
}

@Composable
@DevicePreviews
fun previewNewsItemCompose() {
    NewsItemCompose(
        imageUrl = "https://img.dummyapi.io/photo-1557976606-d068b9719915.jpg",
        title = "title",
        description = "desc",
        tag = "dog",
        dateText = "25-05-2023",
        onClick = {}
    )
}