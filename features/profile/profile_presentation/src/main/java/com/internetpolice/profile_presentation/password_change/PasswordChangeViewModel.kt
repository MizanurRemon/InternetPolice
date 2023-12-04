package com.internetpolice.profile_presentation.password_change

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.auth_presentation.isPasswordValid
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import com.internetpolice.profile_domain.use_cases.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordChangeViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
) : ViewModel() {
    var state by mutableStateOf(PasswordChangeState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: PasswordChangeEvent) {
        when (event) {

            is PasswordChangeEvent.OnPasswordEnter -> {
                state = state.copy(
                    password = event.password
                )
            }

            is PasswordChangeEvent.OnNewPasswordEnter -> {
                state = state.copy(
                    newPassword = event.newPassword
                )
            }

            is PasswordChangeEvent.OnConfirmPasswordEnter -> {
                state = state.copy(
                    confirmPassword = event.confirmPassword
                )
            }

            is PasswordChangeEvent.OnSubmitClickForPasswordReset -> {
                viewModelScope.launch {
                    if (state.password.isEmpty()) {
                        state = state.copy(
                            isPasswordError = true,
                            isNewPasswordError = false,
                            isConfirmPasswordError = false,
                        )
                        return@launch
                    }

                    if (!isPasswordValid(state.newPassword)) {
                        state = state.copy(
                            isNewPasswordError = true,
                            isPasswordError = false,
                            isConfirmPasswordError = false,

                            )
                        return@launch
                    }

                    if (!isPasswordValid(state.confirmPassword)) {
                        state = state.copy(
                            isConfirmPasswordError = true,
                            isPasswordError = false,
                            isNewPasswordError = false,
                        )
                        return@launch
                    }

                    if (state.newPassword != state.confirmPassword) {
                        state = state.copy(
                            isConfirmPasswordError = false
                        )
                        return@launch
                    }

                    state = state.copy(
                        isShowDialog = true,
                        isNewPasswordError = false,
                        isPasswordError = false,
                        isConfirmPasswordError = false
                    )


                    profileUseCases.passwordResetUseCase(
                        state.password, state.newPassword
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
            }
        }

    }
}


