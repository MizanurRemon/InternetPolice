package com.internetpolice.report_presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import com.internetpolice.report_domain.model.ReportModel
import com.internetpolice.report_domain.use_cases.ReportUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportUseCases: ReportUseCases,
) : ViewModel() {
    var state by mutableStateOf(ReportState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: ReportEvent) {
        when (event) {
            is ReportEvent.OnSearchChange -> {
                state = state.copy(searchText = event.partialText)
            }
            is ReportEvent.OnDescriptionInput -> {
                state = state.copy(description = event.description)
            }
            is ReportEvent.OnSelectedReportType -> {
                state = state.copy(reportType = event.type)
            }

            is ReportEvent.OnSelectCategory -> {
                state = state.copy(category = event.category)
            }
            is ReportEvent.OnSelectedDomain -> {
                state = state.copy(
                    selectedDomain = event.domainModel, selectedDomainId = event.domainModel.id
                )
            }
            is ReportEvent.OnSearchResult -> {
                var searchValue = state.searchText.replace("www.", "", true)
                if (searchValue.isEmpty()) state =
                    state.copy(domainList = emptyList(), isSearching = false)

                if (searchValue.length < 3) return
                state = state.copy(domainList = emptyList(), isSearching = true)

                viewModelScope.launch {
                    reportUseCases.getDomainUseCase(state.searchText.replace("www.", "", true))
                        .onSuccess {
                            state = state.copy(domainList = it, isSearching = false)

                        }.onFailure {
                            state = state.copy(domainList = emptyList(), isSearching = false)

                        }
                }
            }
            is ReportEvent.OnReportTypeResult -> {

            }
            is ReportEvent.OnSubmitReport -> {
                viewModelScope.launch {
                    state = state.copy(isShowLoading = true)

                    reportUseCases.reportDomainUseCase(
                        ReportModel(
                            category = state.category,
                            domainStoreId = state.selectedDomain!!.id ,
                            trustTemplateId = 1,
                            voteType = state.reportType,
                            voteStatus = state.voteStatus,
                            description = state.description ?: "",
                            domainName =state.selectedDomain!!.domain

                        )
                    ).onSuccess {
                        state = state.copy(isShowLoading = false)
                        _uiEvent.send(
                            UiEvent.Success
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

            is ReportEvent.OnBack -> viewModelScope.launch { _uiEvent.send(UiEvent.NavigateUp) }

        }
    }
}

