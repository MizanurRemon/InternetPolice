package com.internetpolice.profile_presentation.avater

sealed class AvatarEvent {
    data class OnNameChange(val name: String) : AvatarEvent()
    data class OnSubmitAvatar(val avatarData: AvatarData) : AvatarEvent()
    object OnSubmit : AvatarEvent()
}
