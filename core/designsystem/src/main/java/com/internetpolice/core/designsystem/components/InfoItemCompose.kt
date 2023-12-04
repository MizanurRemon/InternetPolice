package com.internetpolice.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.bodyLightTextStyle
import com.internetpolice.core.designsystem.w

@Composable
fun InfoItemCompose(
    @StringRes titleStringResId: Int,
    @DrawableRes iconResId: Int, modifier: Modifier = Modifier
) {
    return Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 35.w(), top = 8.h(), end = 20.w()),
        verticalAlignment = Alignment.Top,
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(22.r())
        )
        Spacer(modifier = Modifier.width(12.w()))
        Text(text = stringResource(id = titleStringResId), style = bodyLightTextStyle)
    }
}