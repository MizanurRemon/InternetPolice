package com.internetpolice.settings_presentation.screens.report_problem

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.core.common.util.UiEvent
import com.internetpolice.core.common.util.UiText
import com.internetpolice.settings_domain.use_cases.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportProblemViewModel @Inject constructor(
    private val settingsUseCase: SettingsUseCase,
) : ViewModel() {
    var state by mutableStateOf(ReportProblemState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: ReportProblemEvent) {
        when (event) {

            is ReportProblemEvent.OnSubjectSelect -> {
                state = state.copy(
                    subject = event.subject
                )
            }

            is ReportProblemEvent.OnDescriptionEnter -> {
                state = state.copy(
                    description = event.description
                )
            }

            is ReportProblemEvent.OnSubmitClick -> {
                if (state.description.isNotEmpty()) {
                    state = state.copy(isShowDialog = true, isError = false)

                    viewModelScope.launch {
                        settingsUseCase.reportProblemUseCase(
                            state.description,
                            state.subject
                        ).onSuccess {
                            state = state.copy(isShowDialog = false, description = "")
                            _uiEvent.send(
                                UiEvent.Success
                            )
                        }.onFailure {
                            state = state.copy(isShowDialog = false)
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.DynamicString(
                                        it.message ?: "Sign In Please"
                                    )
                                )
                            )
                        }
                    }
                } else {
                    state = state.copy(isError = true)
                }

            }

        }
    }
}