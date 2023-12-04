package com.internetpolice.auth_presentation.login

sealed class LoginEvent {
    data class OnEmailEnter(val email: String): LoginEvent()
    data class OnPasswordEnter(val password: String): LoginEvent()
    object OnSubmitClick: LoginEvent()
    object OnSignUpClick: LoginEvent()
}
