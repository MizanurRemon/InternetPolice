package com.internetpolice.profile_presentation.acc_settings

sealed class AccSettingsEvent {
    data class OnNameChange(val name: String) : AccSettingsEvent()
    data class OnAgeCategoryChange(val category: String) : AccSettingsEvent()
    object OnSubmit : AccSettingsEvent()
    object OnDeleteAccount : AccSettingsEvent()
}
