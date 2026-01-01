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
}
