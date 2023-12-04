package com.internetpolice.profile_presentation.email_update

data class SendEmailOTPState(
    val isShowDialog: Boolean = false,
    val restartTimer: Boolean = false,
    val isError: Boolean = false,
    val email: String = "",
    val isEmailValid: Boolean = true,
)
