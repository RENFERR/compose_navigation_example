package com.example.test.ui.screen.recipient

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.test.ui.screen.recipient.RecipientUiState
import com.example.test.ui.theme.TestProjectTheme
import java.util.UUID
import kotlin.random.Random
import com.example.test.ui.screen.recipient.RecipientUiEvent as Event
import com.example.test.ui.screen.recipient.RecipientUiState as State

@Composable
fun RecipientScreen(
    viewModel: RecipientVMImpl = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateBackResult: (List<User>) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    RecipientScreen(
        state = state,
        onEvent = viewModel::handleUiEvent,
        onBack = navigateBack,
        onReady = navigateBackResult
    )
}

@Composable
private fun RecipientScreen(
    state: State,
    onEvent: (Event) -> Unit,
    onBack: () -> Unit,
    onReady: (List<User>) -> Unit
) = Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is RecipientUiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )

            is RecipientUiState.Success -> {
                Users(
                    modifier = Modifier.fillMaxSize(),
                    users = state.users,
                    onAdd = { onEvent.invoke(Event.AddUser(it)) },
                    onRemove = { onEvent.invoke(Event.RemoveUser(it)) }
                )
                BackButton(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    onBack = onBack
                )
                ReadyButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                    onReady = {
                        onReady.invoke(state.users)
                    }
                )
            }
        }

    }
}

@Composable
private fun Users(
    modifier: Modifier = Modifier,
    users: List<User>,
    onAdd: (User) -> Unit,
    onRemove: (User) -> Unit
) = LazyColumn(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    item {
        Button(
            onClick = {
                onAdd.invoke(
                    User(
                        id = Random.nextLong(),
                        name = UUID.randomUUID().toString()
                    )
                )
            }
        ) {
            Text(
                text = "Add user",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
    items(items = users.toList(), key = { it.id }) { user ->
        UserItem(
            name = user.name,
            onClick = { onRemove.invoke(user) }
        )
    }
}

@Composable
private fun ReadyButton(modifier: Modifier = Modifier, onReady: () -> Unit) =
    TextButton(modifier = modifier, onClick = onReady) {
        Text(
            text = "Ready",
            style = MaterialTheme.typography.labelLarge
        )
    }

@Composable
private fun BackButton(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) = IconButton(modifier = modifier, onClick = onBack) {
    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
}

@Preview(name = "light", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "dark", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RecipientScreenPreview() {
    TestProjectTheme {
        RecipientScreen(
            state = State.Loading,
            onEvent = {},
            onBack = {},
            onReady = {}
        )
    }
}