package com.internetpolice.auth_presentation.screens

data class SignUpNotAvailableState(
    val email: String = "",
    val isEmailValid: Boolean = true,
    val isSignUpNotAvailableDialogOpen: Boolean = false
)
