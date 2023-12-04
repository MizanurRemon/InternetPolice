package com.internetpolice.profile_presentation.email_update

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.auth.auth_domain.use_cases.AuthUseCases
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SendEmailOTPViewModel @Inject constructor(private val authUseCases: AuthUseCases) :
    ViewModel() {
    var state by mutableStateOf(SendEmailOTPState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun isEmailValid() {
        if (state.email.isEmpty()) return
        state = state.copy(isEmailValid = authUseCases.emailValidate(state.email))
    }

    fun onEvent(event: SendEmailOTPEvent) {
        when (event) {

            is SendEmailOTPEvent.OnEmailEnter -> {
                state = state.copy(
                    email = event.email
                )
            }


            is SendEmailOTPEvent.OnVerifyClick -> {
                state = state.copy(isShowDialog = true, isError = false)

                viewModelScope.launch {
                    authUseCases.resendVerificationEmilUseCase(state.email, true).onSuccess {
                        state = state.copy(isShowDialog = false, restartTimer = true)
                        _uiEvent.send(
                            UiEvent.Success
                        )
                    }.onFailure {
                        _uiEvent.send(
                            UiEvent.ShowSnackbar(
                                UiText.DynamicString(
                                    it.message ?: ""
                                )
                            )
                        )
                        state = state.copy(isShowDialog = false, restartTimer = true)
                    }
                }
            }

            else -> {}
        }
    }

}