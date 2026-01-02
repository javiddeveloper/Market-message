package xyz.sattar.javid.marketmessage.ui.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    @SerialName("home")
    data object Home : Screen()

    @Serializable
    @SerialName("messages")
    data object Messages : Screen()

    @Serializable
    @SerialName("settings")
    data object Settings : Screen()

    // Nested Graph for Message Creation
    @Serializable
    @SerialName("message_creation_graph")
    data object MessageCreationGraph : Screen()

    @Serializable
    @SerialName("create_message")
    data object CreateMessage : Screen()

    @Serializable
    @SerialName("select_contacts")
    data object SelectContacts : Screen()

    @Serializable
    @SerialName("edit_contacts")
    data object EditContacts : Screen()

    @Serializable
    @SerialName("send_message")
    data object SendMessage : Screen()
}
