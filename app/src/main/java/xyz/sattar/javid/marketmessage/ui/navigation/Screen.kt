package xyz.sattar.javid.marketmessage.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()
    @Serializable
    data object Messages : Screen()
    @Serializable
    data object Settings : Screen()

    // Nested Graph for Message Creation
    @Serializable
    data object MessageCreationGraph : Screen()

    @Serializable
    data object CreateMessage : Screen()

    @Serializable
    data object SelectContacts : Screen()

    @Serializable
    data object EditContacts : Screen()

    @Serializable
    data object SendMessage : Screen()
}
