package com.internetpolice.news_presentation.NewsDetails

data class NewsDetailsState(
    val isShowDialog: Boolean = false,
    var title: String = "",
    var author: String = "",
    var tag: String = "",
    var date: String = "",
    var content: String = "",
    var image: String = "",
    var newsLink: String = ""
)
