package com.internetpolice.auth_presentation.verify

data class VerifyEmailOtpState(
    val digit1: String = "",
    val digit2: String = "",
    val digit3: String = "",
    val digit4: String = "",
    val digit5: String = "",
    val isError: Boolean = false,
    val isShowDialog: Boolean = false,
    val restartTimer: Boolean = false,
    val newEmail: String = "",
    val source: String = "",
    val confirmEmail: String = "",
    val userId : Int = 0
    )
