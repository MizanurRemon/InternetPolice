package com.internetpolice.auth_presentation.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.auth.auth_domain.model.UserWaitingModel
import com.internetpolice.auth.auth_domain.use_cases.AuthUseCases
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpNotAvailableViewModel @Inject constructor(private val authUseCases: AuthUseCases) :
    ViewModel() {
    var state by mutableStateOf(SignUpNotAvailableState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun isEmailValid() {
        if (state.email.isEmpty()) return
        state = state.copy(isEmailValid = authUseCases.emailValidate(state.email))
    }

    fun onEvent(event: SignUpNotAvailableEvent) {
        when (event) {

            is SignUpNotAvailableEvent.OnEmailEnter -> {
                state = state.copy(email = event.email)
            }

            is SignUpNotAvailableEvent.OnYesClick -> {
                viewModelScope.launch {
                    authUseCases.userWaitingUseCases(UserWaitingModel(email = state.email))
                        .onSuccess {
                            state = state.copy(isSignUpNotAvailableDialogOpen = false)
                            _uiEvent.send(UiEvent.Success)

                        }.onFailure {
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.DynamicString(
                                        it.message ?: ""
                                    )
                                )
                            )
                        }
                }
            }

            is SignUpNotAvailableEvent.OnContinueClick -> {
                viewModelScope.launch {
                    state = state.copy(
                        isEmailValid = authUseCases.emailValidate(state.email),
                        isSignUpNotAvailableDialogOpen = authUseCases.emailValidate(state.email)
                    )
                }
            }

            else -> {}
        }
    }
}