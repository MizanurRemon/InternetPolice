package com.internetpolice.app.connectivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.designScreenWidth
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.bodyRegularTextStyle
import com.internetpolice.core.designsystem.theme.subHeading1TextStyle
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignR

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkStatusScreen() {
    val context = LocalContext.current
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCallback = remember { NetworkCallbackImpl() }
    val isNetworkAvailable by networkCallback.isNetworkAvailable

    DisposableEffect(connectivityManager) {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    if (!isNetworkAvailable) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = AppBrush)
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 18.dp, end = 18.dp),
                floatingActionButton = {
                    AppActionButtonCompose(stringId = CommonR.string.reload, modifier = Modifier.padding(36.dp)) {
                        if (!isNetworkAvailable) {

                            context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))

                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.Center
            ) {
                Column(
                    // verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(modifier = Modifier) {
                        Image(
                            painter = painterResource(id = DesignR.drawable.ic_no_internet_bg),
                            contentDescription = "",
                        )
                        Column(
                            modifier = Modifier
                                .width((designScreenWidth / 1.7).dp)
                                .align(alignment = Alignment.BottomCenter)
                                .padding(bottom = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = CommonR.string.no_active_internet_connection),
                                modifier = Modifier.padding(bottom = 50.dp),
                                style = subHeading1TextStyle.copy(fontSize = 28.sp),
                            )

                            Text(
                                text = stringResource(id = CommonR.string.no_internet_connection_text),
                                textAlign = TextAlign.Center,
                                style = bodyRegularTextStyle
                            )
                        }
                    }
                }
            }
        }

    }

}