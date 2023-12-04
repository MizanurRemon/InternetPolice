package com.internetpolice.profile_presentation.progress

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.capitalizeFirstCharacter
import com.internetpolice.database.dao.UserDao
import com.internetpolice.profile_domain.use_cases.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases, private val userDao: UserDao,

    ) : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            state = state.copy(isShowDialog = true)

            profileUseCases.progressUseCase().onSuccess {
                state = state.copy(
                    isShowDialog = false,
                    votes = it.votes,
                    points = it.points
                )
            }.onFailure {
                state = state.copy(isShowDialog = false)
            }

            getUserInfo()

        }
    }

    private suspend fun getUserInfo() = withContext(Dispatchers.IO) {
        userDao.getUsers().collect {
            if (it.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    state = state.copy(
                        name = it[0].nickname.toString(),
                        url = it[0].avatarImagePath ?: "",
                        userScore = it[0].totalPoints
                    )
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnSelectedPoint -> {
                state = state.copy(selectedPoint = event.point)
            }
        }
    }
}


