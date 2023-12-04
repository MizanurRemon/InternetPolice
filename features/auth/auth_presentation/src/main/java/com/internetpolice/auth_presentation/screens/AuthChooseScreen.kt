package com.internetpolice.auth_presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.designsystem.components.AppActionButtonCompose
import com.internetpolice.core.designsystem.components.LoadingDialog
import com.internetpolice.core.designsystem.h
import com.internetpolice.core.designsystem.languageList
import com.internetpolice.core.designsystem.theme.AppBrush
import com.internetpolice.core.designsystem.theme.ColorPrimaryDark
import com.internetpolice.core.designsystem.w
import com.internetpolice.core.ui.DevicePreviews
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR


@Composable
fun AuthChooseScreen(
    onGuestClick: () -> Unit,
    onSignUp: () -> Unit,
    onSignIn: () -> Unit,
    navigateSignUpNotAvailable: () -> Unit,
    viewModel: RegAllowViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {

                    //uncomment this during using testing api
                    if (viewModel.state.isRegAllowed) {
                        onSignUp()
                    } else {
                        navigateSignUpNotAvailable()
                    }

                    //uncomment this during using staging api
                    //onSignUp()
                }

                is UiEvent.ShowSnackbar -> {

                }

                is UiEvent.NavigateUp -> {

                }
            }

        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppBrush)
            .padding(start = 18.dp, end = 18.dp, top = 80.h())
    ) {
        val (titleImage, centerImage, signupBtn, loginBtn, guest, space) = createRefs()
        Image(painter = if (viewModel.state.tag == languageList[1].tag) {
            painterResource(id = DesignSystemR.drawable.ic_logo_horizontal_dutch)
        } else {
            painterResource(id = DesignSystemR.drawable.ic_logo_horizontal)
        },
            contentDescription = null,
            modifier = Modifier
                .constrainAs(titleImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(start = 18.w(), end = 18.w())
                .fillMaxWidth()
                .background(Color.Transparent)
                .wrapContentHeight())
        Image(painter = painterResource(id = DesignSystemR.drawable.ic_cop_intro),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 45.h())
                .constrainAs(centerImage) {
                    top.linkTo(titleImage.bottom)
                    bottom.linkTo(signupBtn.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
//                .width(306.w())
//                .height(405.h())
                .scale(1f)
                .background(Color.Transparent),
            contentScale = ContentScale.FillBounds)
        AppActionButtonCompose(
            stringId = CommonR.string.sign_up_for_free,
            modifier = Modifier
                .padding(top = 33.h(), start = 18.w(), end = 18.w())
                .constrainAs(signupBtn) {
                    bottom.linkTo(loginBtn.top)
                }
        ) {

            viewModel.onEvent(AuthChooseEvent.OnSignUpClick)

        }

        AppActionButtonCompose(
            stringId = CommonR.string.i_already_have_an_account,
            bgColor = Color.White,
            borderColor = ColorPrimaryDark,
            textColor = ColorPrimaryDark,
            modifier = Modifier
                .padding(top = 33.h(), start = 18.w(), end = 18.w())
                .constrainAs(loginBtn) {
                    bottom.linkTo(guest.top)
                }
        ) {
            onSignIn()
        }
        Row(modifier = Modifier
            .padding(bottom = 33.h(), top = 33.h())
            .constrainAs(guest) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .clickable {
                onGuestClick()
            }) {
            /* Text(
                 text = stringResource(id = CommonR.string.i_would_like_to),
                 color = Color(0xff818181)
             )
             Spacer(modifier = Modifier.width(5.w()))
             Text(
                 text = stringResource(id = CommonR.string.continue_as_a_guest),
                 color = ColorPrimaryDark,
                 fontWeight = FontWeight.W700
             )*/
        }
    }

    if (viewModel.state.isShowDialog)
        LoadingDialog {}


}

@Composable
@DevicePreviews
fun PreviewAuthChooseScreen() {
    AuthChooseScreen(onGuestClick = { },
        onSignUp = { },
        onSignIn = {},
        navigateSignUpNotAvailable = {})
}