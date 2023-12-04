package com.internetpolice.profile_presentation.progress

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.internetpolice.profile_domain.model.UserPointModel
import com.internetpolice.profile_domain.model.VoteModel


data class ProfileState(
    val name: String = "",
    val url: String = "",
    val userScore: Int = 0,
    val selectedPoint: Int = 0,
    val votes: List<VoteModel> = ArrayList(),
    val points: List<UserPointModel> = ArrayList(),
    val isShowDialog: Boolean = false,
    val isPointSelected: MutableState<Boolean> = mutableStateOf(true),
    val isReportSelected: MutableState<Boolean> = mutableStateOf(false)
)
