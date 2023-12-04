package com.internetpolice.auth_presentation.forgot_pass

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
class ForgotPasswordViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
) : ViewModel() {
    var state by mutableStateOf(ForgotPasswordState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {

            is ForgotPasswordEvent.OnEmailEnter -> {
                state = state.copy(
                    email = event.email
                )
            }
            is ForgotPasswordEvent.OnPasswordEnter -> {
                state = state.copy(
                    password = event.password
                )
            }
            is ForgotPasswordEvent.OnConfirmPasswordEnter -> {
                state = state.copy(
                    confirmPassword = event.confirmPassword
                )
            }

            is ForgotPasswordEvent.OnSubmitClickForLinkSend -> {
                if (authUseCases.emailValidate(state.email)) {
                    state = state.copy(isShowDialog = true, isError = false)

                    viewModelScope.launch {
                        authUseCases.resetPasswordLinkSendUseCase(
                            state.email
                        ).onSuccess {
                            state = state.copy(isShowDialog = false)
                            _uiEvent.send(
                                UiEvent.Success
                            )
                        }.onFailure {
                            state = state.copy(isShowDialog = false)
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.DynamicString(
                                        it.message ?: ""
                                    )
                                )
                            )
                        }
                    }
                } else {
                    state = state.copy(isError = true)
                }

            }

            is ForgotPasswordEvent.OnSubmitClickForPasswordReset -> {
                if (authUseCases.passwordValidate(state.password) && authUseCases.passwordValidate(
                        state.confirmPassword
                    ) && state.password == state.confirmPassword
                ) {
                    state = state.copy(isShowDialogForPasswordReset = true, isPasswordError=false,isConfirmPasswordError = false)

                    viewModelScope.launch {
                        authUseCases.resetPasswordUseCase(
                            state.password, state.confirmPassword
                        ).onSuccess {
                            state = state.copy(isShowDialogForPasswordReset = false)
                            _uiEvent.send(
                                UiEvent.Success
                            )
                        }.onFailure {
                            state = state.copy(isShowDialogForPasswordReset = false)
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.DynamicString(
                                        it.message ?: ""
                                    )
                                )
                            )
                        }
                    }
                } else {
                    state = state.copy(isPasswordError=true,isConfirmPasswordError = true)
                }

            }

        }
    }
}