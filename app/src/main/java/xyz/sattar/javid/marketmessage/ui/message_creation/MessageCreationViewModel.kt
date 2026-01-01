package xyz.sattar.javid.marketmessage.ui.message_creation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MessageCreationViewModel @Inject constructor() : ViewModel() {

    private val _messageBody = MutableStateFlow("")
    val messageBody = _messageBody.asStateFlow()

    private val _selectedContacts = MutableStateFlow<List<String>>(emptyList())
    val selectedContacts = _selectedContacts.asStateFlow()

    fun updateMessageBody(body: String) {
        _messageBody.update { body }
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

    fun clearData() {
        _messageBody.value = ""
        _selectedContacts.value = emptyList()
    }
}
