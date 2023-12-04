package com.internetpolice.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.languageList
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.report_domain.model.DomainModel
import kotlinx.coroutines.delay
import com.internetpolice.core.designsystem.R as DesignSystemR

@Composable
fun SplashScreen(
    navController: NavController,
    domainModel: DomainModel? = null,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->

            when (event) {
                is UiEvent.Success -> {
                    delay(2000L)
                    if (domainModel != null)
                        navController.navigate(Route.WEB_SEARCH_REPORT + "/${domainModel}") {
                            popUpTo(navController.graph.id)
                        }
                    else
                        navController.navigate(Route.HOME) {
                            popUpTo(navController.graph.id)
                        }
                }

                is UiEvent.ShowSnackbar -> {
                }

                is UiEvent.NavigateUp -> {
                    delay(2000L)
                    navController.navigate(Route.LANGUAGE_INIT) {
                        popUpTo(navController.graph.id) {}
                    }
                }
            }

        }
    }

    Image(
        painter = painterResource(
            id = if (viewModel.state.tag == languageList[1].tag) {
                DesignSystemR.drawable.ic_logo_vertical_dutch
            } else {
                DesignSystemR.drawable.ic_logo_vertical
            }
        ),
        contentDescription = "",
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(brush = AppBrush)
    )
}