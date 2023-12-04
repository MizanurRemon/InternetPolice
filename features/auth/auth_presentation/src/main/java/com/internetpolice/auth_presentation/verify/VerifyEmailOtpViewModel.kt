package com.internetpolice.auth_presentation.verify

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.auth.auth_domain.model.UpdateEmailModel
import com.internetpolice.auth.auth_domain.use_cases.AuthUseCases
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import com.internetpolice.database.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyEmailOtpViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    savedStateHandle: SavedStateHandle,
    private val userDao: UserDao
) : ViewModel() {
    var state by mutableStateOf(VerifyEmailOtpState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        state = state.copy(
            newEmail = savedStateHandle["email"]!!,
            source = savedStateHandle["source"]!!
        )
    }

    fun timerReset() {
        state = state.copy(restartTimer = false)
    }

    fun onEvent(event: VerifyEmailOtpEvent) {
        when (event) {
            is VerifyEmailOtpEvent.OnDigit1Enter -> {
                state = state.copy(
                    digit1 = event.digit1
                )
            }

            is VerifyEmailOtpEvent.OnDigit2Enter -> {
                state = state.copy(
                    digit2 = event.digit2
                )
            }

            is VerifyEmailOtpEvent.OnDigit3Enter -> {
                state = state.copy(
                    digit3 = event.digit3
                )
            }

            is VerifyEmailOtpEvent.OnDigit4Enter -> {
                state = state.copy(
                    digit4 = event.digit4
                )
            }

            is VerifyEmailOtpEvent.OnDigit5Enter -> {
                state = state.copy(
                    digit5 = event.digit5
                )
            }

            is VerifyEmailOtpEvent.OnResendClick -> {
                state = state.copy(isShowDialog = true, isError = false)

                if (state.source == Route.SIGN_IN || state.source == Route.SIGN_UP) {
                    viewModelScope.launch {
                        authUseCases.draftVerificationUseCases().onSuccess {
                            state = state.copy(isShowDialog = false, restartTimer = true)
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.DynamicString(
                                        "an OTP has been sent to the mail"
                                    )
                                )
                            )
                        }.onFailure {
                            state = state.copy(isShowDialog = false, restartTimer = true)
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
                    viewModelScope.launch {
                        authUseCases.resendVerificationEmilUseCase(state.newEmail, true).onSuccess {
                            state = state.copy(isShowDialog = false, restartTimer = true)
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
            }

            is VerifyEmailOtpEvent.OnSubmitClick -> {
                if (state.digit1.length == 1 && state.digit2.length == 1 &&
                    state.digit3.length == 1 && state.digit4.length == 1 && state.digit5.length == 1
                ) {
                    state = state.copy(isShowDialog = true, isError = false)

                    viewModelScope.launch(Dispatchers.IO) {

                        val token = state.digit1 +
                                state.digit2 + state.digit3 + state.digit4 + state.digit5

                        if (state.source == Route.SIGN_IN || state.source == Route.SIGN_UP) {
                            authUseCases.draftUserTokenVerifyUseCase(state.newEmail, token)
                                .onSuccess {
                                    state = state.copy(isShowDialog = false)
                                    _uiEvent.send(
                                        UiEvent.Success
                                    )

                                }.onFailure { err ->
                                    state = state.copy(isShowDialog = false)
                                    _uiEvent.send(
                                        UiEvent.ShowSnackbar(
                                            UiText.DynamicString(
                                                err.message ?: ""
                                            )
                                        )
                                    )
                                }

                        } else {
                            authUseCases.verifyTokenUseCase(
                                token
                            ).onSuccess {


                                if (state.source == Route.SET_NEW_EMAIL) {

                                    authUseCases.updateEmailUseCase(
                                        state.newEmail
                                    ).onSuccess {
                                        state = state.copy(isShowDialog = false)
                                        _uiEvent.send(
                                            UiEvent.Success
                                        )
                                    }.onFailure { err ->
                                        state = state.copy(isShowDialog = false)
                                        _uiEvent.send(
                                            UiEvent.ShowSnackbar(
                                                UiText.DynamicString(
                                                    err.message ?: ""
                                                )
                                            )
                                        )
                                    }

                                } else {
                                    state = state.copy(isShowDialog = false)
                                    _uiEvent.send(
                                        UiEvent.Success
                                    )
                                }

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


                    }
                } else {
                    state = state.copy(isError = true)
                }

            }
        }
    }
}