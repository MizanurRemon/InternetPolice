package com.internetpolice.notification_presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.datastore.PreferenceDataStoreConstants
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,

    ) : ViewModel() {

    var state by mutableStateOf(NotificationState())
        private set


    init {
        state = state.copy(
            tag = AppCompatDelegate.getApplicationLocales().toLanguageTags(),
        )
    }

    fun updateNotificationStatus(status: Boolean) {

        viewModelScope.launch {
            preferenceDataStoreHelper.putPreference(
                PreferenceDataStoreConstants.IS_Notification_Enable, status
            )
        }
    }

}
