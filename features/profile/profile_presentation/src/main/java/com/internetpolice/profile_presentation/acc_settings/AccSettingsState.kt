package com.internetpolice.profile_presentation.acc_settings



data class AccSettingsState(
    val name:String="",
    val email:String="",
    val ageCategory:String="",
    val isShowLoading: Boolean = false,
    val isError: Boolean = false,

)
