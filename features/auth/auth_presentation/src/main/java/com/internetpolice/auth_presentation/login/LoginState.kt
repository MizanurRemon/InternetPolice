package com.internetpolice.auth_presentation.login

import com.internetpolice.core.common.util.DEFAULT_LANGUAGE_TAG

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isShowDialog: Boolean = false,
    val isRegAllowed: Boolean = false,
    val tag: String = DEFAULT_LANGUAGE_TAG,
    val isDraftUser : Boolean = false
)
