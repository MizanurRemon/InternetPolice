package com.internetpolice.settings_presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.internetpolice.settings_presentation.model.ChangeLogResponse
import com.internetpolice.core.designsystem.components.AppToolbarCompose
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.r
import com.internetpolice.core.designsystem.theme.*
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.settings_presentation.model.ChangeLogDetailsListResponse

import com.internetpolice.core.common.R as CommonR

@Composable
fun ChangeLogDetailsScreen(
    versionName: String,
    changeLogDetailsListResponse: ChangeLogDetailsListResponse,
    onBack: () -> Unit
) {

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
                .padding(start = 16.w(), top = 24.h(), end = 16.w())

        ) {
            AppToolbarCompose(title = stringResource(id = CommonR.string.back)) {
                onBack()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 42.h(), start = 20.w(), end = 20.w())
            ) {
                Text(
                    text = stringResource(id = CommonR.string.whats_new),
                    style = heading1TextStyle
                )

                Divider(color = Color.Transparent, modifier = Modifier.height(10.h()))

                Text(
                    text = buildVersion.value.toString(),
                    style = bodyMediumTextStyle.copy(color = Color.Gray)
                )

                Divider(color = Color.Transparent, modifier = Modifier.height(20.h()))

                Text(
                    text = stringResource(id = CommonR.string.initial_build),
                    style = bodyMediumTextStyle.copy(color = Color.Black)
                )
                /*LazyColumn() {
                    items(changeLogDetailsListResponse.changeLogResponseList) { response ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.w(), top = 20.h(), bottom = 20.w()),
                            horizontalArrangement = Arrangement.Start
                        ) {

                            Spacer(
                                modifier = Modifier
                                    .padding(end = 10.w(), top = 10.h())
                                    .background(color = ColorTextPrimary, shape = CircleShape)
                                    .size(5.r())
                            )
                            Text(
                                text = response.msg,
                                style = bodyRegularTextStyle.copy(
                                    color = ColorTextSecondary,
                                    textAlign = TextAlign.Start
                                )
                            )
                        }
                    }
                }*/
            }
        }
    }
}

@DevicePreviews
@Composable
fun PreviewChangeLogDetails() {
    val changeLogDetailsListResponse = getTestChangeLogDetailsListResponse()
    ChangeLogDetailsScreen(versionName = "", changeLogDetailsListResponse) {

    }
}

@Composable
fun getTestChangeLogDetailsListResponse(): ChangeLogDetailsListResponse {
    return ChangeLogDetailsListResponse(
        version =
        "", changeLogResponseList = listOf(
            ChangeLogResponse(stringResource(id = CommonR.string.lorem_ipsum)),
            ChangeLogResponse(stringResource(id = CommonR.string.lorem_ipsum)),
            ChangeLogResponse(stringResource(id = CommonR.string.lorem_ipsum)),
            ChangeLogResponse(stringResource(id = CommonR.string.lorem_ipsum)),
            ChangeLogResponse(stringResource(id = CommonR.string.lorem_ipsum)),
            ChangeLogResponse(stringResource(id = CommonR.string.lorem_ipsum)),
            ChangeLogResponse(stringResource(id = CommonR.string.lorem_ipsum))
        )
    )
}
