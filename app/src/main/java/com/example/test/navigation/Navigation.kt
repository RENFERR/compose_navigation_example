package com.example.test.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.test.model.User
import com.example.test.serialize
import com.example.test.ui.screen.presenter.PresenterScreen
import com.example.test.ui.screen.presenter.PresenterUiEvent
import com.example.test.ui.screen.presenter.PresenterVMImpl
import com.example.test.ui.screen.recipient.RecipientScreen

const val presenterNavigationRoute = "presenter_route"
const val recipientNavigationRoute = "recipient_route"

const val usersArg = "users"

fun NavController.navigateRecipientScreen(initialUsers: List<User>? = null) {
    if (initialUsers.isNullOrEmpty()) navigate(recipientNavigationRoute)
    else navigate("$recipientNavigationRoute?$usersArg=${initialUsers.serialize()}")
}

private fun NavController.navigateBackToPresenter(users: List<User>) {
    popBackStack()
    currentBackStackEntry
        ?.savedStateHandle
        ?.set(usersArg, users)
}

fun NavGraphBuilder.navigateForResultFeature(
    navController: NavController
) {
    presenterScreen(navigateRecipient = navController::navigateRecipientScreen)
    recipientScreen(
        navigateBack = navController::navigateUp,
        navigateBackResult = navController::navigateBackToPresenter
    )
}

fun NavGraphBuilder.presenterScreen(
    navigateRecipient: (List<User>) -> Unit
) = composable(route = presenterNavigationRoute) {navBackEntry ->
    val viewModel: PresenterVMImpl = hiltViewModel()
    PresenterScreen(
        viewModel = viewModel,
        navigateRecipient = navigateRecipient
    )
    LaunchedEffect(navBackEntry) {
        snapshotFlow {
            navBackEntry.savedStateHandle.get<List<User>>(usersArg) ?: emptyList()
        }.collect { users ->
            viewModel.handleUiEvent(PresenterUiEvent.UpdateUsers(users = users))
        }
    }
}

fun NavGraphBuilder.recipientScreen(
    navigateBack: () -> Unit,
    navigateBackResult: (List<User>) -> Unit
) = composable(route = "$recipientNavigationRoute?$usersArg={$usersArg}") {
    RecipientScreen(navigateBack = navigateBack, navigateBackResult = navigateBackResult)
}