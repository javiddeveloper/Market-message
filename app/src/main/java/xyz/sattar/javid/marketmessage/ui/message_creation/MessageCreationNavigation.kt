package xyz.sattar.javid.marketmessage.ui.message_creation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import xyz.sattar.javid.marketmessage.ui.navigation.Screen

fun NavGraphBuilder.messageCreationGraph(navController: NavHostController) {
    navigation<Screen.MessageCreationGraph>(startDestination = Screen.CreateMessage) {
        composable<Screen.CreateMessage> { entry ->
            val viewModel = hiltViewModel<MessageCreationViewModel>(
                remember(entry) { navController.getBackStackEntry(Screen.MessageCreationGraph) }
            )
            CreateMessageScreen(
                viewModel = viewModel,
                onNext = { navController.navigate(Screen.SelectContacts) }
            )
        }
        composable<Screen.SelectContacts> { entry ->
            val viewModel = hiltViewModel<MessageCreationViewModel>(
                remember(entry) { navController.getBackStackEntry(Screen.MessageCreationGraph) }
            )
            SelectContactsScreen(
                viewModel = viewModel,
                onNext = { navController.navigate(Screen.EditContacts) }
            )
        }
        composable<Screen.EditContacts> { entry ->
            val viewModel = hiltViewModel<MessageCreationViewModel>(
                remember(entry) { navController.getBackStackEntry(Screen.MessageCreationGraph) }
            )
            EditContactsScreen(
                viewModel = viewModel,
                onNext = { navController.navigate(Screen.SendMessage) }
            )
        }
        composable<Screen.SendMessage> { entry ->
            val viewModel = hiltViewModel<MessageCreationViewModel>(
                remember(entry) { navController.getBackStackEntry(Screen.MessageCreationGraph) }
            )
            SendMessageScreen(
                viewModel = viewModel,
                onFinish = {
                    navController.popBackStack(Screen.MessageCreationGraph, inclusive = true)
                }
            )
        }
    }
}
