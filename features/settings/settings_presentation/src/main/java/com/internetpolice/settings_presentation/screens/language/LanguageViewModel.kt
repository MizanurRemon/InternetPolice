package com.internetpolice.settings_presentation.screens.language

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.toLowerCase
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.core.common.util.DEFAULT_LANGUAGE_TAG
import com.internetpolice.core.common.util.LanguageTagEnum
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.designsystem.components.LanguageResponse
import com.internetpolice.core.designsystem.languageList
import com.internetpolice.settings_domain.use_cases.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private val settingsUseCase: SettingsUseCase
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(languageList[0])
        private set

    var isShowDialog = mutableStateOf(false)

    fun onBack() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    init {
        viewModelScope.launch {
            val tag = preferenceDataStoreHelper.getFirstPreference(
                PreferenceDataStoreConstants.LANGUAGE_TAG,
                DEFAULT_LANGUAGE_TAG
            )

            val language = languageList.find { it.tag == tag } ?: languageList[1]
            state = language
            preferenceDataStoreHelper.putPreference(
                PreferenceDataStoreConstants.LANGUAGE_TAG,
                state.tag
            )
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(
                    state.tag
                )
            )

        }
    }

    fun updateState(languageResponse: LanguageResponse) {
        viewModelScope.launch {
            state = languageResponse

        }
    }

    fun onNext() {
        viewModelScope.launch {

            val isLoggedIn = preferenceDataStoreHelper.getFirstPreference(
                PreferenceDataStoreConstants.IS_LOGGED_IN,
                false
            )

            if (isLoggedIn) {

                isShowDialog.value = true

                settingsUseCase.updateLanguageUseCases(
                    LanguageTagEnum.values().first { it.tag == state.tag }.name.lowercase(
                        Locale.ROOT
                    )
                ).onSuccess {
                    isShowDialog.value = false
                    preferenceDataStoreHelper.putPreference(
                        PreferenceDataStoreConstants.LANGUAGE_TAG,
                        state.tag
                    )
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(
                            state.tag
                        )
                    )
                    _uiEvent.send(UiEvent.Success)
                }.onFailure {
                    isShowDialog.value = false
                }
            } else {
                preferenceDataStoreHelper.putPreference(
                    PreferenceDataStoreConstants.LANGUAGE_TAG,
                    state.tag
                )
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(
                        state.tag
                    )
                )
                _uiEvent.send(UiEvent.Success)
            }


        }
    }

}