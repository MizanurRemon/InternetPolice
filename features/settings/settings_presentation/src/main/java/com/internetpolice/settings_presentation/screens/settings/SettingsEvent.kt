package com.internetpolice.settings_presentation.screens.settings

sealed class SettingsEvent {
    data class OnNotificationClick(val isOn: Boolean): SettingsEvent()
}
