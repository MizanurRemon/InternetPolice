package com.internetpolice.auth_presentation.forgot_pass

data class ForgotPasswordState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isConfirmPasswordError: Boolean = false,
    val isShowDialog: Boolean = false,
    val isShowDialogForPasswordReset: Boolean = false,
)
