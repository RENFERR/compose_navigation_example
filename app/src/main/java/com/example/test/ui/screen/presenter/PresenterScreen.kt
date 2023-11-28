package com.example.test.ui.screen.presenter

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.test.model.User
import com.example.test.ui.screen.components.UserItem
import com.example.test.ui.screen.presenter.PresenterUiState
import com.example.test.ui.theme.TestProjectTheme
import com.example.test.ui.screen.presenter.PresenterUiEvent as Event
import com.example.test.ui.screen.presenter.PresenterUiState as State

@Composable
fun PresenterScreen(
    viewModel: PresenterVMImpl = hiltViewModel(),
    navigateRecipient: (List<User>) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    PresenterScreen(
        state = state,
        onEvent = viewModel::handleUiEvent,
        navigateRecipient = navigateRecipient
    )
}

@Composable
private fun PresenterScreen(
    state: State,
    onEvent: (Event) -> Unit,
    navigateRecipient: (List<User>) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (state) {
            is PresenterUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is PresenterUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Button(onClick = { navigateRecipient.invoke(state.users) }) {
                            Text(
                                text = "Select users",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                    item {
                        Button(onClick = { onEvent.invoke(Event.ChangeState) }) {
                            Text(
                                text = "Change state",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                    item {
                        Text(
                            text = "State = ${state.state}",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    items(
                        items = state.users,
                        key = { it.id }
                    ) {(_, name) ->
                        UserItem(name = name, onClick = {})
                    }
                }
            }
        }
    }
}

@Preview(name = "light", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "dark", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PresenterScreenPreview() {
    TestProjectTheme {
        PresenterScreen(
            state = State.Loading,
            onEvent = {},
            navigateRecipient = {}
        )
    }
}