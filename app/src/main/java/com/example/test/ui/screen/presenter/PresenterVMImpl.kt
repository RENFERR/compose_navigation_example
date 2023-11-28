package com.example.test.ui.screen.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PresenterVMImpl @Inject constructor() : PresenterVM, ViewModel() {

    private val stateFlow = MutableStateFlow("Initial")
    private val usersFlow = MutableStateFlow<List<User>>(emptyList())

    override val uiState: StateFlow<PresenterUiState> = combine(
        flow = stateFlow,
        flow2 = usersFlow,
        transform = { state, users ->
            PresenterUiState.Success(
                state = state,
                users = users
            )
        }
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = PresenterUiState.Loading
    )

    override fun handleUiEvent(uiEvent: PresenterUiEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is PresenterUiEvent.ChangeState -> stateFlow.emit(UUID.randomUUID().toString())
                is PresenterUiEvent.UpdateUsers -> usersFlow.emit(uiEvent.users)
            }
        }
    }
}