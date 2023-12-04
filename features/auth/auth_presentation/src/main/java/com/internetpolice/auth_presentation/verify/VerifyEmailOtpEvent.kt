package com.internetpolice.auth_presentation.verify

sealed class VerifyEmailOtpEvent {
    data class OnDigit1Enter(val digit1: String): VerifyEmailOtpEvent()
    data class OnDigit2Enter(val digit2: String): VerifyEmailOtpEvent()
    data class OnDigit3Enter(val digit3: String): VerifyEmailOtpEvent()
    data class OnDigit4Enter(val digit4: String): VerifyEmailOtpEvent()
    data class OnDigit5Enter(val digit5: String): VerifyEmailOtpEvent()
    object OnSubmitClick: VerifyEmailOtpEvent()
    object OnResendClick: VerifyEmailOtpEvent()
}
