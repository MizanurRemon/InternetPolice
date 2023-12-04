package com.internetpolice.profile_presentation.email_update

sealed class SendEmailOTPEvent{
    object OnVerifyClick: SendEmailOTPEvent()
    data class OnEmailEnter(val email: String): SendEmailOTPEvent()
}
