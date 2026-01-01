package xyz.sattar.javid.marketmessage.ui.message_creation.createMessage

import xyz.sattar.javid.marketmessage.domain.model.ReadyMessage

sealed interface CreateMessageIntent {
    data object LoadReadyMessages : CreateMessageIntent
    data class AddReadyMessage(val content: String) : CreateMessageIntent
    data class DeleteReadyMessage(val id: Long) : CreateMessageIntent
    data class SelectMessage(val id: Long, val content: String) : CreateMessageIntent
    data object GoToSelectContacts : CreateMessageIntent
    data object GoBack : CreateMessageIntent
}

data class CreateMessageState(
    val messageBody: String = "",
    val readyMessages: List<ReadyMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    sealed interface PartialState {
        data class ReadyMessagesLoaded(val messages: List<ReadyMessage>) : PartialState
        data class Loading(val isLoading: Boolean) : PartialState
        data class Error(val message: String) : PartialState
    }
}

sealed interface CreateMessageEvent {
    data object NavigateToSelectContacts : CreateMessageEvent
    data object NavigateBack : CreateMessageEvent
}
