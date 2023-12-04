package com.internetpolice.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.ColorPrimaryDark
import com.internetpolice.core.designsystem.theme.fontRoboto
import com.internetpolice.core.designsystem.theme.subHeading1TextStyle
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun CopActionCompose(
    title: String,
    actionTitle: String,
    @DrawableRes bgId: Int,
    actionButtonColor: Color = ColorPrimaryDark,
    onActionButtonClick: () -> Unit,
    favIconUrl: String
) {

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = favIconUrl)
            .apply(block = fun ImageRequest.Builder.() {
                error(DesignSystemR.drawable.earthicon)
            }).build()
    )

    return Box {
        Image(
            painter = painterResource(id = bgId),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .width(255.w())
                .padding(end = 25.w())
                .align(alignment = Alignment.CenterEnd),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                if (favIconUrl.isNotEmpty()) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp),
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                }

                Text(
                    title,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = subHeading1TextStyle.copy(
                        color = Color.White,
                        shadow = Shadow(
                            color = Color(0xFF9EAEE8),
                            offset = Offset(0f, 5.0f),
                            blurRadius = 3f
                        )
                    ),
                )
            }
            Spacer(modifier = Modifier.height(20.h()))
            Button(
                onClick = onActionButtonClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = actionButtonColor,
                    disabledContainerColor = actionButtonColor
                ),
                modifier = Modifier
                    .height(34.h())
                    .fillMaxWidth()
                    .align(Alignment.End)
                    .border(
                        width = 1.5.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(7.dp)
                    )
                    .background(color = actionButtonColor, RoundedCornerShape(7.dp))
                    .clip(RoundedCornerShape(7.dp)),
                contentPadding = PaddingValues(0.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(10.w()))
                    Text(
                        text = actionTitle, style =
                        TextStyle(
                            fontFamily = fontRoboto,
                            fontSize = 14.ssp(),
                            fontWeight = FontWeight.W700,
                            lineHeight = 23.ssp(),
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = DesignSystemR.drawable.ic_arrow_right),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.w()))
                }

            }
        }
    }
}

@Composable
@Preview
private fun CopActionCompose() {
    CopActionCompose(
        title = "Hi, I’m agent Jim",
        actionTitle = "Check your progress",
        bgId = DesignSystemR.drawable.ic_police_home,
        onActionButtonClick = {
        },
        favIconUrl = ""
    )
}