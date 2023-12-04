package com.internetpolice.profile_presentation.progress

sealed class ProfileEvent {
    data class OnSelectedPoint(val point: Int) : ProfileEvent()
}
