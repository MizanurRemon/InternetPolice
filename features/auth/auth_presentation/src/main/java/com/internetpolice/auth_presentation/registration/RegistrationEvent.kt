package com.internetpolice.auth_presentation.registration

sealed class RegistrationEvent {
    data class OnEmailEnter(val email: String): RegistrationEvent()
    data class OnPasswordEnter(val password: String): RegistrationEvent()
    data class OnConfirmationPasswordEnter(val confirmPassword: String): RegistrationEvent()
    data class OnAgeEnter(val age: String): RegistrationEvent()
    data class OnAcceptTermsEnter(val isAccepted: Boolean): RegistrationEvent()
    object OnSubmitClick: RegistrationEvent()

}
