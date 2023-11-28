package com.example.test.ui.screen.presenter

import com.example.test.model.User
import kotlinx.coroutines.flow.StateFlow

interface PresenterVM {
    val uiState: StateFlow<PresenterUiState>
    fun handleUiEvent(uiEvent: PresenterUiEvent)
}

sealed class PresenterUiState {
    object Loading : PresenterUiState()
    data class Success(val users: List<User>, val state: String) : PresenterUiState()
}

sealed class PresenterUiEvent {
    object ChangeState : PresenterUiEvent()
    data class UpdateUsers(val users: List<User>) : PresenterUiEvent()
}