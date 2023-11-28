package com.example.test.ui.screen.recipient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.navigation.arg.RecipientNavigationArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipientVMImpl @Inject constructor(
    arg: RecipientNavigationArg
) : RecipientVM, ViewModel() {

    private val selectedUsersFlow = MutableStateFlow(value = arg.users ?: emptyList())

    override val uiState: StateFlow<RecipientUiState> = selectedUsersFlow.map { users ->
        RecipientUiState.Success(users = users)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = RecipientUiState.Loading
    )

    override fun handleUiEvent(uiEvent: RecipientUiEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is RecipientUiEvent.AddUser -> selectedUsersFlow.update { it.plus(uiEvent.user) }
                is RecipientUiEvent.RemoveUser -> selectedUsersFlow.update { it.minus(uiEvent.user) }
            }
        }
    }
}