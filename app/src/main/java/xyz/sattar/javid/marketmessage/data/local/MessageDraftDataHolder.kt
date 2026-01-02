package xyz.sattar.javid.marketmessage.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import xyz.sattar.javid.marketmessage.domain.model.DraftContact

object MessageDraftDataHolder {
    private val _messageBody = MutableStateFlow("")
    val messageBody = _messageBody.asStateFlow()

    private val _messageId = MutableStateFlow<Long?>(null)
    val messageId = _messageId.asStateFlow()

    private val _selectedContacts = MutableStateFlow<List<DraftContact>>(emptyList())
    val selectedContacts = _selectedContacts.asStateFlow()

    fun updateMessageBody(body: String) {
        _messageBody.value = body
    }

    fun updateMessageId(id: Long?) {
        _messageId.value = id
    }

    fun toggleContactSelection(contact: DraftContact) {
        _selectedContacts.update { currentList ->
            if (currentList.any { it.phoneNumber == contact.phoneNumber }) {
                currentList.filter { it.phoneNumber != contact.phoneNumber }
            } else {
                currentList + contact
            }
        }
    }

    fun updateContactName(phoneNumber: String, newName: String) {
        _selectedContacts.update { currentList ->
            currentList.map {
                if (it.phoneNumber == phoneNumber) {
                    it.copy(name = newName)
                } else {
                    it
                }
            }
        }
    }

    fun clearDraft() {
        _messageBody.value = ""
        _messageId.value = null
        _selectedContacts.value = emptyList()
    }
}
