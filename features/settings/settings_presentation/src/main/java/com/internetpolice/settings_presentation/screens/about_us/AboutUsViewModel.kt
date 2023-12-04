package com.internetpolice.settings_presentation.screens.about_us

import androidx.appcompat.app.AppCompatDelegate
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
class AboutUsViewModel
@Inject constructor(private val webPageUseCase: SettingsUseCase) : ViewModel() {
    var state by mutableStateOf(AboutUsState())
        private set


    init {
        getAboutUs()
    }

    private fun getAboutUs() {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            state = state.copy(
                tag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
            )

            /*webPageUseCase.getAboutUs().onSuccess {
                state = state.copy(isLoading = false, text = it.text)

            }.onFailure {
                state = state.copy(isLoading = false)
            }*/
        }
    }
}