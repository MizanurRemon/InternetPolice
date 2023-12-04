package com.internetpolice.auth_presentation.forgot_pass

sealed class ForgotPasswordEvent {
    data class OnEmailEnter(val email: String): ForgotPasswordEvent()
    object OnSubmitClickForLinkSend: ForgotPasswordEvent()
    object OnSubmitClickForPasswordReset: ForgotPasswordEvent()
    data class OnPasswordEnter(val password: String): ForgotPasswordEvent()
    data class OnConfirmPasswordEnter(val confirmPassword: String): ForgotPasswordEvent()

}
