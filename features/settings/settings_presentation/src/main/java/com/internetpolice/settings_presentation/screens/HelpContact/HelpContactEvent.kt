package com.internetpolice.settings_presentation.screens.HelpContact


sealed class HelpContactEvent{
    data class OnDescriptionEnter(val description: String): HelpContactEvent()
    object OnSubmitClick: HelpContactEvent()
}
