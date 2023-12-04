package com.internetpolice.settings_presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.ssp
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import java.util.*
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChangeLogScreen(
    versionName: String,
    onBack: () -> Unit,
    onItemClick: () -> Unit
) {
    var context = LocalContext.current

    val buildVersion = remember {
        mutableStateOf(versionName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush),

        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.w(), top = 24.h(), end = 16.w())
        ) {
            AppToolbarCompose(title = stringResource(id = CommonR.string.back)) {
                onBack()
            }
            Text(
                text = stringResource(id = CommonR.string.changelog),
                style = heading1TextStyle,
                modifier = Modifier
                    .padding(top = 120.h(), bottom = 24.h())
                    .fillMaxWidth()
            )
            ChangeLogItemCompose(
                stringResource(id = CommonR.string.version) + " " + buildVersion.value.toString(),
                onItemClick
            )
        }
    }
}


@Composable
fun ChangeLogItemCompose(
    title: String,
    onItemClick: () -> Unit
) {
    val cardElevation = .5

    Card(colors = CardDefaults
        .elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation.dp),
        modifier = Modifier
            .padding(top = 15.h())
            .noRippleClickable {
                onItemClick()
            }) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.r()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Text(
                modifier = Modifier.padding(start = 30.w()),
                text = title, style = TextStyle(
                    color = ColorPrimaryDark,
                    fontSize = 16.ssp(),
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = DesignSystemR.drawable.ic_chevron_right),
                tint = Color.LightGray,
                modifier = Modifier.size(16.r()),
                contentDescription = ""
            )
        }
    }
}


@Composable
@DevicePreviews
fun PreviewChangeLogScreen() {
    ChangeLogScreen(versionName = "", onBack = {}, onItemClick = {})
}
