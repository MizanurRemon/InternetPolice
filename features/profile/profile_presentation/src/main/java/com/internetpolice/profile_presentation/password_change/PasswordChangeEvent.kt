package com.internetpolice.profile_presentation.password_change

sealed class PasswordChangeEvent {
    object OnSubmitClickForPasswordReset: PasswordChangeEvent()
    data class OnPasswordEnter(val password: String): PasswordChangeEvent()
    data class OnNewPasswordEnter(val newPassword: String): PasswordChangeEvent()
    data class OnConfirmPasswordEnter(val confirmPassword: String): PasswordChangeEvent()
}
