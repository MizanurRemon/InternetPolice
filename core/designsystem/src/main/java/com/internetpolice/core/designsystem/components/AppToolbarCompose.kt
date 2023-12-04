package com.internetpolice.core.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.internetpolice.core.designsystem.theme.bodyXSBoldTextStyle
import com.internetpolice.core.designsystem.w


@Composable
fun AppToolbarCompose(
    title: String,
    modifier: Modifier=Modifier,onBackClick: () -> Unit
) {
    return Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.ArrowBack,
            contentDescription = title,
            tint = Color(0xff7F87A5),
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onBackClick()
                },
        )
        Spacer(modifier = Modifier.width(16.w()))
        Text(text = title, style = bodyXSBoldTextStyle)
    }
}