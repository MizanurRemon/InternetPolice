package com.internetpolice.auth_presentation.registration

data class RegistrationState(
    val ageCategory: String = "60+",
    val confirmPassword: String = "",
    val email: String = "",
    val isAgreedToTermsAndConditions: Boolean = false,
    val language: String = "",
    val password: String = "",
    val token: String = "",
    val showDialog: Boolean = false,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isConfirmPasswordValid: Boolean = true,
    val isTCValid: Boolean = true
)
