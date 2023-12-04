package com.internetpolice.news_presentation.News

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.core.common.util.capitalizeFirstCharacter
import com.internetpolice.news_domain.model.NewsResponse
import com.internetpolice.news_domain.use_cases.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {
    var state by mutableStateOf(NewsState())
        private set

    private val tabItemsNames = mutableSetOf<String>()

    private var newsList = mutableListOf<NewsResponse>()

    init {
        getNews()
    }

    private fun getNews() {
        viewModelScope.launch() {
            state = state.copy(
                isShowDialog = true,
            )
            newsUseCases.getNewsData().onSuccess {

                newsList = it.toMutableList()
                state = state.copy(
                    isShowDialog = false,
                    newsList = newsList
                )

                if (state.newsList.isNotEmpty()){
                    tabItemsNames.add("All")
                }

                for (i in state.newsList.indices) {
                    tabItemsNames.add(state.newsList[i].tag)
                }

                state = state.copy(tabItemsNames = tabItemsNames)

            }.onFailure {
                state = state.copy(isShowDialog = false)

            }
        }
    }


    fun onEvent(event: NewsEvent) {

        when (event) {

            is NewsEvent.OnTabClick -> {
                Log.d("dataxx", "onEvent: ${event.name}")
                state = if (event.name == "All") {
                    state.copy(
                        newsList = newsList
                    )
                } else {
                    state.copy(
                        newsList = newsList.filter {
                            it.tag.contains(
                                event.name
                            )
                        }.map { it }.toMutableList()
                    )
                }


            }

        }
    }
}

