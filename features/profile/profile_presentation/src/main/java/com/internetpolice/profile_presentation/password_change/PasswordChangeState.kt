package com.internetpolice.profile_presentation.password_change

data class PasswordChangeState(
    val password: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isNewPasswordError: Boolean = false,
    val isConfirmPasswordError: Boolean = false,
    val isShowDialog: Boolean = false,
)
