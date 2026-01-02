package xyz.sattar.javid.marketmessage.ui.home

import xyz.sattar.javid.marketmessage.domain.model.HomeStatistics

data class HomeState(
    val statistics: HomeStatistics? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

sealed interface HomeIntent {
    data object LoadStatistics : HomeIntent
    data object CreateNewMessage : HomeIntent
}

sealed interface HomeEvent {
    data object NavigateToCreateMessage : HomeEvent
}
