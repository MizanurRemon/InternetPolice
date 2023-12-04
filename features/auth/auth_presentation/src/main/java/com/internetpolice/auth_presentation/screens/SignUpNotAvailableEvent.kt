package com.internetpolice.auth_presentation.screens

sealed class SignUpNotAvailableEvent {
    data class OnEmailEnter(val email: String) : SignUpNotAvailableEvent()
    object OnContinueClick : SignUpNotAvailableEvent()
    object OnYesClick: SignUpNotAvailableEvent()
}
