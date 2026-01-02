package xyz.sattar.javid.marketmessage.ui.message_creation.editContacts

import xyz.sattar.javid.marketmessage.domain.model.DraftContact

sealed interface EditContactsIntent {
    data object LoadContacts : EditContactsIntent
    data class RemoveContact(val contact: DraftContact) : EditContactsIntent
    data class UpdateContactName(val contact: DraftContact, val newName: String) : EditContactsIntent
    data object GoToSend : EditContactsIntent
    data object GoBack : EditContactsIntent
}

data class EditContactsState(
    val selectedContacts: List<DraftContact> = emptyList()
) {
    sealed interface PartialState {
        data class ContactsLoaded(val contacts: List<DraftContact>) : PartialState
    }
}

sealed interface EditContactsEvent {
    data object NavigateToSend : EditContactsEvent
    data object NavigateBack : EditContactsEvent
}
