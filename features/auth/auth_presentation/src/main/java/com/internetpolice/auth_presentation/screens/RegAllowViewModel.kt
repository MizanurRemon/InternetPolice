package com.internetpolice.auth_presentation.screens

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.auth.auth_domain.use_cases.AuthUseCases
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import com.internetpolice.core.designsystem.languageList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegAllowViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {
    var state by mutableStateOf(RegAllowState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    init {
        state = state.copy(
            tag = AppCompatDelegate.getApplicationLocales().toLanguageTags(),
        )
    }

    private fun regAllowCheck() {
        viewModelScope.launch {

            state = state.copy(
                isShowDialog = true
            )

            authUseCases.registrationAllowUseCases().onSuccess {

                state = state.copy(isShowDialog = false, isRegAllowed = it)
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

    fun onEvent(event: AuthChooseEvent) {
        when (event) {
            is AuthChooseEvent.OnSignUpClick -> {
                regAllowCheck()
            }
        }
    }
}