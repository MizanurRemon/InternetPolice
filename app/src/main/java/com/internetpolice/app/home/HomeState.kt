package com.internetpolice.app.home

import com.internetpolice.news_domain.model.NewsResponse
import com.internetpolice.report_domain.model.DomainModel

data class HomeState(
    val newsList: List<NewsResponse> = emptyList(),
    val isShowDialog: Boolean = false,
    val isLogOutLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isProfileComplete: Boolean = false,
    val userName: String = "",
    val rank: String = "",
    val avatarUrl:String="",
    val userScore:Int=0,
    val domainList: List<DomainModel> = emptyList(),
    val searchText: String = ""
)
