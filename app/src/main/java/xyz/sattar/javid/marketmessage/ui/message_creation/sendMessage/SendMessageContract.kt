package xyz.sattar.javid.marketmessage.ui.message_creation.sendMessage

import xyz.sattar.javid.marketmessage.domain.model.DraftContact

sealed interface SendMessageIntent {
    data object LoadDraft : SendMessageIntent
    data object GotoDashboard : SendMessageIntent
    data class SendSingleSms(val contact: DraftContact) : SendMessageIntent
    data object SendBulkSms : SendMessageIntent
    data object GoBack : SendMessageIntent
}

data class SendMessageState(
    val messageBody: String = "",
    val contacts: List<DraftContact> = emptyList(),
    val sentPhoneNumbers: Set<String> = emptySet(),
    val isSending: Boolean = false,
    val error: String? = null
) {
    val contactCount: Int get() = contacts.size

    sealed interface PartialState {
        data class DraftLoaded(val body: String, val contacts: List<DraftContact>) : PartialState
        data class Sending(val isSending: Boolean) : PartialState
        data class SmsSent(val phoneNumber: String) : PartialState
        data class Error(val message: String) : PartialState
    }
}

sealed interface SendMessageEvent {
    data object NavigateBack : SendMessageEvent
    data object NavigateDashboard : SendMessageEvent
}
