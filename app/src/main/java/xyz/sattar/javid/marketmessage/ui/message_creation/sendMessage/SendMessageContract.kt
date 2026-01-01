package xyz.sattar.javid.marketmessage.ui.message_creation.sendMessage

sealed interface SendMessageIntent {
    data object LoadDraft : SendMessageIntent
    data object SendMessage : SendMessageIntent
    data object GoBack : SendMessageIntent
}

data class SendMessageState(
    val messageBody: String = "",
    val contactCount: Int = 0,
    val isSending: Boolean = false,
    val error: String? = null
) {
    sealed interface PartialState {
        data class DraftLoaded(val body: String, val contactCount: Int) : PartialState
        data class Sending(val isSending: Boolean) : PartialState
        data class Error(val message: String) : PartialState
        data object Sent : PartialState
    }
}

sealed interface SendMessageEvent {
    data object NavigateBack : SendMessageEvent
    data object MessageSent : SendMessageEvent
}
