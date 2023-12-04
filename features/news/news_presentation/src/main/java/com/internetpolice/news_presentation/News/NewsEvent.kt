package com.internetpolice.news_presentation.News

sealed class NewsEvent{
    data class OnTabClick(val name: String) : NewsEvent()
}
