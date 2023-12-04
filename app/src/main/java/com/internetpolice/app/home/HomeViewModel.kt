package com.internetpolice.app.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.app.home.use_cases.HomeUseCases
import com.internetpolice.core.common.util.DEFAULT_LANGUAGE_TAG
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.database.dao.UserDao
import com.internetpolice.profile_domain.use_cases.ProfileUseCases
import com.internetpolice.report_domain.use_cases.ReportUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val reportUseCases: ReportUseCases,
    private val profileUseCases: ProfileUseCases,
    private val userDao: UserDao,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,

    ) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(HomeState())
        private set

    init {
        state = state.copy(
            isShowDialog = true,
        )
        viewModelScope.launch {
            val isLoggedIn = preferenceDataStoreHelper.getFirstPreference(
                PreferenceDataStoreConstants.IS_LOGGED_IN, false
            )
            state = state.copy(isLoggedIn = isLoggedIn)

            launch { updateNewsData() }

            if (isLoggedIn) {
                launch { getUserInfo() }
                launch { updateScore() }
            }
        }
    }

    private suspend fun updateScore() = withContext(Dispatchers.IO) {
        profileUseCases.updateScoreUseCase()
    }

    private suspend fun updateNewsData() = withContext(Dispatchers.IO) {
        preferenceDataStoreHelper.getPreference(
            PreferenceDataStoreConstants.LANGUAGE_TAG, DEFAULT_LANGUAGE_TAG
        ).collect {

            withContext(Dispatchers.Main) {
                state = state.copy(isShowDialog = true, newsList = emptyList())
            }

            homeUseCases.getNewsData().onSuccess {
                withContext(Dispatchers.Main) {
                    state = state.copy(
                        isShowDialog = false, newsList = it
                    )
                }

            }.onFailure {
                withContext(Dispatchers.Main) {
                    state = state.copy(isShowDialog = false)
                }

            }
        }

    }

    private suspend fun getUserInfo() = withContext(Dispatchers.IO) {
        userDao.getUsers().collect {
            if (it.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    state = state.copy(
                        userName = it[0].nickname.toString(),
                        avatarUrl = it[0].avatarImagePath ?: "",
                        isProfileComplete = it[0].isProfileCompleted,
                        userScore = it[0].totalPoints
                    )
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnSearchResult -> {
                state = state.copy(searchText = event.partialText)

                if (state.searchText.isEmpty()) state = state.copy(domainList = emptyList())

                if (state.searchText.length < 3) return
                state = state.copy(domainList = emptyList())

                viewModelScope.launch {
                    reportUseCases.getDomainUseCase(state.searchText).onSuccess {
                        state = state.copy(domainList = it)

                    }.onFailure {
                        state = state.copy(domainList = emptyList())

                    }
                }
            }

            is HomeEvent.OnLogOut -> {

                state = state.copy(isLogOutLoading = true)

                viewModelScope.launch {
                    profileUseCases.logOutUseCase().onSuccess {
                        state = state.copy(isLogOutLoading = false)
                        _uiEvent.send(
                            UiEvent.Success
                        )
                    }.onFailure {
                        state = state.copy(isLogOutLoading = false)
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
