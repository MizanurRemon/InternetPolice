package com.internetpolice.settings_presentation.screens.faq

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.settings_domain.use_cases.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FaqViewModel @Inject constructor(private val webPageUseCase: SettingsUseCase) : ViewModel() {

    var state by mutableStateOf(FaqState())
        private set

    init {
        getFaqs()
    }

    private fun getFaqs() {
        state = state.copy(isLoading = true)

        viewModelScope.launch {

            webPageUseCase.getFaqList().onSuccess {
                state = state.copy(isLoading = false, questionList = it)
            }.onFailure {
                state = state.copy(isLoading = false)

            }

        }


    }
}