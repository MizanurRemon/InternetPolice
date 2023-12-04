package com.internetpolice.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.internetpolice.core.designsystem.theme.ColorTextPrimary
import com.internetpolice.core.designsystem.theme.conditional
import com.internetpolice.core.designsystem.theme.largeBodyTextStyle

@Composable
fun AgeGroupComposeSignup(
    itemList: List<String>,
    selectedItem: String,
    modifier: Modifier,
    ageCategoryErrorState: MutableState<String>,
    size: Dp = 70.dp,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemList.forEachIndexed { _, item ->
            Box(
                modifier = modifier
                    .size(size)
                    .clip(RoundedCornerShape(18.dp))
                    .conditional(item != selectedItem) {
                        return@conditional border(
                            width = 1.dp,
                            color =
                            Color(0xFFE2E4EA),

                            shape = RoundedCornerShape(18.dp)
                        ).background(color = Color.White)
                    }
                    .conditional(item == selectedItem) {
                        return@conditional background(color = ColorTextPrimary)
                    }
                    .clip(RoundedCornerShape(15.dp))
                    .clickable {
                        onClick(item)
                    }
            ) {
                Text(
                    text = item,
                    modifier = Modifier.align(alignment = Alignment.Center),
                    style = if (item != selectedItem)
                        largeBodyTextStyle.copy(color = Color(0xff4B598F))
                    else largeBodyTextStyle.copy(color = Color.White),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}