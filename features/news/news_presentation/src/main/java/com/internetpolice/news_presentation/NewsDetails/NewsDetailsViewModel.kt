package com.internetpolice.news_presentation.NewsDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.news_domain.use_cases.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(NewsDetailsState())
        private set

    init {
        val newsID: Int? = savedStateHandle["id"]
        if (newsID != null) {
            getNewsDetails(newsID)
        }
    }


    private fun getNewsDetails(id: Int) {
        state = state.copy(isShowDialog = true)
        viewModelScope.launch {
            newsUseCases.getNewsDetails(id).onSuccess {
                state = state.copy(
                    isShowDialog = false,
                    title = it.title,
                    author = it.authorName,
                    date = it.date,
                    tag = it.tag,
                    content = it.content,
                    image = it.image,
                    newsLink = it.newsLink
                )

            }.onFailure {
                state = state.copy(isShowDialog = false)
            }
        }
    }

}