package com.internetpolice.auth_presentation.login

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.internetpolice.auth.auth_domain.model.AuthenticationModel
import com.internetpolice.auth.auth_domain.model.LoginModel
import com.internetpolice.auth.auth_domain.model.RegistrationModel
import com.internetpolice.auth.auth_domain.use_cases.AuthUseCases
import com.internetpolice.auth_presentation.registration.RegistrationEvent
import com.internetpolice.auth_presentation.registration.RegistrationState
import com.internetpolice.core.common.navigation.Route
import com.internetpolice.core.common.util.LanguageListEnum
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private val authUseCases: AuthUseCases,
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun isEmailValid() {
        if (state.email.isEmpty()) return
        state = state.copy(isEmailValid = authUseCases.emailValidate(state.email))
    }

    fun signUpAvailabilityCheck(navController: NavController) {
        viewModelScope.launch {
            state = state.copy(isShowDialog = true)
            authUseCases.registrationAllowUseCases().onSuccess {
                state = state.copy(isShowDialog = false, isRegAllowed = it)

                if (state.isRegAllowed) navController.navigate(Route.SIGN_UP) else navController.navigate(
                    Route.SIGN_UP_NOT_AVAILABLE
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


    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailEnter -> {
                state = state.copy(
                    email = event.email
                )
            }

            is LoginEvent.OnPasswordEnter -> {
                state = state.copy(password = event.password)
            }

            is LoginEvent.OnSubmitClick -> {
                signIn()
            }

            is LoginEvent.OnSignUpClick -> {
                // signUpAvailabilityCheck()
            }

        }
    }

    private fun signIn() {
        viewModelScope.launch {
            if (authUseCases.emailValidate(state.email) && state.password.isNotEmpty()) {
                state = state.copy(
                    isShowDialog = true,
                    isEmailValid = true,
                    isPasswordValid = true,
                )
                authUseCases.loginUseCase(
                    LoginModel(
                        email = state.email,
                        password = state.password,
                    )
                ).onSuccess {

                    state = state.copy(
                        isShowDialog = false,
                        tag = if (it.userProfile.language == "dutch") "nl" else "en",
                        isDraftUser = false
                    )

                    preferenceDataStoreHelper.putPreference(
                        PreferenceDataStoreConstants.LANGUAGE_TAG,
                        state.tag
                    )
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(
                            state.tag
                        )
                    )
                    _uiEvent.send(
                        UiEvent.Success
                    )
                }.onFailure {

                    if (it.message == "404") {
                        authUseCases.authenticationDraftUseCase(
                            AuthenticationModel(
                                username = state.email,
                                password = state.password,
                                rememberMe = false,
                                deviceId = ""
                            )
                        ).onSuccess {

                            state = state.copy(isShowDialog = false, isDraftUser = true)
                            _uiEvent.send(
                                UiEvent.Success
                            )
                        }.onFailure {
                            state = state.copy(isShowDialog = false)
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.DynamicString(
                                        it.message.toString()
                                    )
                                )
                            )
                        }
                    } else {
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
                state = state.copy(
                    isEmailValid = authUseCases.emailValidate(state.email),
                    isPasswordValid = state.password.isNotEmpty(),
                )
            }
        }
    }
}

