package xyz.sattar.javid.marketmessage.ui.message_creation.editContacts

sealed interface EditContactsIntent {
    data object LoadContacts : EditContactsIntent
    data class RemoveContact(val contact: String) : EditContactsIntent
    data object GoToSend : EditContactsIntent
    data object GoBack : EditContactsIntent
}

data class EditContactsState(
    val selectedContacts: List<String> = emptyList()
) {
    sealed interface PartialState {
        data class ContactsLoaded(val contacts: List<String>) : PartialState
    }
}

sealed interface EditContactsEvent {
    data object NavigateToSend : EditContactsEvent
    data object NavigateBack : EditContactsEvent
}
