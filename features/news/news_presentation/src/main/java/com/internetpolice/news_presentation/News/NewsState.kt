package com.internetpolice.news_presentation.News

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.internetpolice.news_domain.model.NewsResponse

data class NewsState(
    val newsList: List<NewsResponse> = emptyList(),
    val isShowDialog: Boolean = false,
    val id: Int = 0,
    val tabItemsNames: Set<String> = emptySet(),
    var tabbedItemIndex : MutableState<Int> = mutableStateOf(0)
)
