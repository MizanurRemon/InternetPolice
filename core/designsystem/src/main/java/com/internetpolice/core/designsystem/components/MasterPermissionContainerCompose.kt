package com.internetpolice.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.internetpolice.core.common.R
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.w


@Composable
fun MasterPermissionContainerCompose(
    onBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppToolbarCompose(
            title = stringResource(id = R.string.back),
            modifier =
            Modifier
                .padding(start = 16.w(), top = 24.h(), end = 16.w(), bottom = 40.h())
                .background(Color.Transparent)
        ) {
            onBack()
        }
        content()
    }
}