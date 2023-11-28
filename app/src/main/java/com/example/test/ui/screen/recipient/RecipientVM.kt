package com.example.test.ui.screen.recipient

import com.example.test.model.User
import kotlinx.coroutines.flow.StateFlow

interface RecipientVM {
    val uiState: StateFlow<RecipientUiState>
    fun handleUiEvent(uiEvent: RecipientUiEvent)
}

sealed class RecipientUiState {
    object Loading : RecipientUiState()
    data class Success(val users: List<User>) : RecipientUiState()
}

sealed class RecipientUiEvent {
    data class RemoveUser(val user: User) : RecipientUiEvent()
    data class AddUser(val user: User) : RecipientUiEvent()
}
