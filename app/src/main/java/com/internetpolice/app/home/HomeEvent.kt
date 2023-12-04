package com.internetpolice.app.home

sealed class HomeEvent {
    data class OnSearchResult(val partialText: String) : HomeEvent()
    object OnLogOut : HomeEvent()

}
