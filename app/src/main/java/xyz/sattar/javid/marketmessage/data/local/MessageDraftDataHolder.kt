package xyz.sattar.javid.marketmessage.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object MessageDraftDataHolder {
    private val _messageBody = MutableStateFlow("")
    val messageBody = _messageBody.asStateFlow()

    private val _messageId = MutableStateFlow<Long?>(null)
    val messageId = _messageId.asStateFlow()

    private val _selectedContacts = MutableStateFlow<List<String>>(emptyList())
    val selectedContacts = _selectedContacts.asStateFlow()

    fun updateMessageBody(body: String) {
        _messageBody.value = body
    }

    fun updateMessageId(id: Long?) {
        _messageId.value = id
    }

    fun toggleContactSelection(contact: String) {
        _selectedContacts.update { currentList ->
            if (currentList.contains(contact)) {
                currentList - contact
            } else {
                currentList + contact
            }
        }
    }

    fun clearDraft() {
        _messageBody.value = ""
        _messageId.value = null
        _selectedContacts.value = emptyList()
    }
}
