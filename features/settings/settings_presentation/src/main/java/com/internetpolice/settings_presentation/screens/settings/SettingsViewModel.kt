package com.internetpolice.settings_presentation.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.core.common.util.DEFAULT_LANGUAGE_TAG
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.designsystem.languageList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,

    ) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(SettingsState())
        private set

    init {
        viewModelScope.launch {

            val isNotificationON = preferenceDataStoreHelper.getFirstPreference(
                PreferenceDataStoreConstants.IS_Notification_Enable, false
            )
            state = state.copy(isNotificationOn = isNotificationON)

            preferenceDataStoreHelper.getPreference(
                PreferenceDataStoreConstants.LANGUAGE_TAG, DEFAULT_LANGUAGE_TAG
            ).collect { tag ->
                val language = languageList.find { it.tag == tag } ?: languageList[1]

                state = state.copy(language = language.title.name)
            }
        }
    }

    fun updateNotificationStatus(status: Boolean) {

        viewModelScope.launch {
            preferenceDataStoreHelper.putPreference(
                PreferenceDataStoreConstants.IS_Notification_Enable, status
            )
            state = state.copy(isNotificationOn = status)

        }
    }

}
