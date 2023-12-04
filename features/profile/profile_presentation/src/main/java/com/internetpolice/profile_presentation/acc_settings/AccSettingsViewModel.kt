package com.internetpolice.profile_presentation.acc_settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import com.internetpolice.core.common.util.capitalizeFirstCharacter
import com.internetpolice.database.dao.UserDao
import com.internetpolice.profile_domain.use_cases.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccSettingsViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases, private val userDao: UserDao,

    ) : ViewModel() {
    var state by mutableStateOf(AccSettingsState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            userDao.getUsers().collect {
                if (it.isNotEmpty()) {
                    state = state.copy(
                        name = it[0].nickname.toString(),
                        ageCategory = it[0].ageCategory ?: "",
                        email = it[0].email
                    )
                }
            }
        }
    }

    fun onEvent(event: AccSettingsEvent) {
        when (event) {
            is AccSettingsEvent.OnNameChange -> {
                state = state.copy(name = event.name)
            }

            is AccSettingsEvent.OnAgeCategoryChange -> {
                state = state.copy(ageCategory = event.category)
            }

            is AccSettingsEvent.OnDeleteAccount -> {
                viewModelScope.launch {
                    state = state.copy(isShowLoading = true, isError = false)

                    profileUseCases.deleteAccountUseCase().onSuccess {
                        state = state.copy(isShowLoading = false)
                        _uiEvent.send(
                            UiEvent.NavigateUp
                        )
                    }.onFailure {
                        state = state.copy(isShowLoading = false)
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

            is AccSettingsEvent.OnSubmit -> {
                viewModelScope.launch {
                    if (state.name.isNotEmpty()) {

                        state = state.copy(isShowLoading = true, isError = false)

                        profileUseCases.basicInfoUpdateUseCase(
                            state.name, state.ageCategory
                        ).onSuccess {
                            state = state.copy(isShowLoading = false)
                            _uiEvent.send(
                                UiEvent.Success
                            )

                        }.onFailure {
                            state = state.copy(isShowLoading = false)
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.DynamicString(
                                        it.message ?: ""
                                    )
                                )
                            )

                        }
                    } else {
                        state = state.copy(isError = true)

                    }
                }

            }
        }
    }
}


