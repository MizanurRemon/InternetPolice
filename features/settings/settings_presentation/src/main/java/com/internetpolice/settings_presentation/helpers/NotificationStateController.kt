package com.internetpolice.settings_presentation.helpers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class NotificationStateController : ViewModel() {
    private var _state: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var notificationState = _state

    fun setState(s: Boolean) {
        _state.value = s
    }

}