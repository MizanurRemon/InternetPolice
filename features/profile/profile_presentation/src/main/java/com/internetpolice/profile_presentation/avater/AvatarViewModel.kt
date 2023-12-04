package com.internetpolice.profile_presentation.avater

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import com.internetpolice.database.dao.UserDao
import com.internetpolice.profile_domain.model.ProfileModel
import com.internetpolice.profile_domain.use_cases.ProfileUseCases

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AvatarViewModel @Inject constructor(
    private val userDao: UserDao,
    private val profileUseCases: ProfileUseCases,
) : ViewModel() {
    var state by mutableStateOf(AvatarState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            launch { getUserInfo() }
        }
    }

    fun onEvent(event: AvatarEvent) {
        when (event) {
            is AvatarEvent.OnNameChange -> {
                state = state.copy(name = event.name)
            }

            is AvatarEvent.OnSubmit -> {
                viewModelScope.launch {
                    if (state.name.isNotEmpty()) {

                        state = state.copy(isShowLoading = true, isError = false)

                        profileUseCases.userNameUpdateUseCase(
                            state.name

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

            is AvatarEvent.OnSubmitAvatar -> {
                viewModelScope.launch {

                    state = state.copy(isShowLoading = true)
                    profileUseCases.profileCompleteUseCase(
                        ProfileModel(
                            gender = event.avatarData.genderType?.name ?: "",
                            color = event.avatarData.skinTone?.name ?: "",
                            faceStyle = event.avatarData.face?.name ?: "",
                            hairStyle = event.avatarData.hairStyleType?.name ?: "",
                            hairColor = event.avatarData.hairColorType?.name ?: ""
                        )
                    ).onSuccess {
                        state = state.copy(
                            isShowLoading = false,
                            url = it.avatarImagePath,
                            name = it.nickname,
                            totalPoints = it.totalPoints
                        )
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

                }

            }
        }

    }

    private suspend fun getUserInfo() = withContext(Dispatchers.IO) {
        userDao.getUsers().collect {
            if (it.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    state = state.copy(
                        userEntity = it.firstOrNull()
                    )
                }
            }
        }
    }
}


