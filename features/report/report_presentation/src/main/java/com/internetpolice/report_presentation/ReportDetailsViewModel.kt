package com.internetpolice.report_presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import com.internetpolice.report_domain.model.DomainModel
import com.internetpolice.report_domain.use_cases.ReportUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ReportDetailsViewModel @AssistedInject constructor(
    private val reportUseCases: ReportUseCases,
    @Assisted
    private val domainModel: DomainModel,
) : ViewModel() {
    var state by mutableStateOf(ReportState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            state = state.copy(isShowLoading = true)

            reportUseCases.getDomainDetailsUseCase(
                domainModel
            ).onSuccess {
                state = state.copy(isShowLoading = false)
                state = state.copy(
                    totalVotes = it.totalVotes,
                    resultMessageKeys = it.resultMessageKeys,
                    voteList = it.voteList
                )

            }.onFailure {
                state = state.copy(isShowLoading = false)
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.DynamicString(
                            it.message ?: ""
                        )
                    )
                )
            }

        }
    }

    @AssistedFactory
    interface Factory {
        fun create(domainModel: DomainModel): ReportDetailsViewModel
    }

    companion object {
        fun provideMainViewModelFactory(
            factory: Factory,
            domainModel: DomainModel,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(domainModel) as T
                }
            }
        }
    }
}

