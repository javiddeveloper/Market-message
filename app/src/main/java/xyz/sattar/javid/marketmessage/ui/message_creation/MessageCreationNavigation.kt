package xyz.sattar.javid.marketmessage.ui.message_creation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import xyz.sattar.javid.marketmessage.ui.message_creation.createMessage.CreateMessageScreen
import xyz.sattar.javid.marketmessage.ui.message_creation.createMessage.CreateMessageViewModel
import xyz.sattar.javid.marketmessage.ui.message_creation.editContacts.EditContactsScreen
import xyz.sattar.javid.marketmessage.ui.message_creation.editContacts.EditContactsViewModel
import xyz.sattar.javid.marketmessage.ui.message_creation.selectContacts.SelectContactsScreen
import xyz.sattar.javid.marketmessage.ui.message_creation.selectContacts.SelectContactsViewModel
import xyz.sattar.javid.marketmessage.ui.message_creation.sendMessage.SendMessageScreen
import xyz.sattar.javid.marketmessage.ui.message_creation.sendMessage.SendMessageViewModel
import xyz.sattar.javid.marketmessage.ui.navigation.Screen

fun NavGraphBuilder.messageCreationGraph(navController: NavHostController) {
    navigation<Screen.MessageCreationGraph>(startDestination = Screen.CreateMessage) {
        composable<Screen.CreateMessage> {
            val viewModel = hiltViewModel<CreateMessageViewModel>()
            CreateMessageScreen(
                viewModel = viewModel,
                onNext = { navController.navigate(Screen.SelectContacts) },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.SelectContacts> {
            val viewModel = hiltViewModel<SelectContactsViewModel>()
            SelectContactsScreen(
                viewModel = viewModel,
                onNext = { navController.navigate(Screen.EditContacts) },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.EditContacts> {
            val viewModel = hiltViewModel<EditContactsViewModel>()
            EditContactsScreen(
                viewModel = viewModel,
                onNext = { navController.navigate(Screen.SendMessage) },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.SendMessage> {
            val viewModel = hiltViewModel<SendMessageViewModel>()
            SendMessageScreen(
                viewModel = viewModel,
                onFinish = {
                    navController.popBackStack(Screen.MessageCreationGraph, inclusive = true)
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
