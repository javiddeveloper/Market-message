package xyz.sattar.javid.marketmessage.ui.message_creation.editContacts

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.sattar.javid.marketmessage.domain.repository.MessageDraftRepository
import xyz.sattar.javid.marketmessage.ui.components.base.BaseViewModel
import javax.inject.Inject

import kotlinx.coroutines.launch
import xyz.sattar.javid.marketmessage.domain.usecase.SaveDraftContactsToCustomersUseCase

@HiltViewModel
class EditContactsViewModel @Inject constructor(
    private val messageDraftRepository: MessageDraftRepository,
    private val saveDraftContactsToCustomersUseCase: SaveDraftContactsToCustomersUseCase
) : BaseViewModel<EditContactsState, EditContactsState.PartialState, EditContactsEvent, EditContactsIntent>(
    initialState = EditContactsState()
) {

    init {
        sendIntent(EditContactsIntent.LoadContacts)
    }

    override fun handleIntent(intent: EditContactsIntent): Flow<EditContactsState.PartialState> =
        flow {
            when (intent) {
                is EditContactsIntent.LoadContacts -> {
                    messageDraftRepository.getSelectedContacts().collect { contacts ->
                        emit(EditContactsState.PartialState.ContactsLoaded(contacts))
                    }
                }

                is EditContactsIntent.RemoveContact -> {
                    messageDraftRepository.toggleContactSelection(intent.contact)
                }

                is EditContactsIntent.UpdateContactName -> {
                    messageDraftRepository.updateContactName(intent.contact.phoneNumber, intent.newName)
                }

                is EditContactsIntent.GoToSend -> {
                    saveDraftContactsToCustomersUseCase()
                    sendEvent(EditContactsEvent.NavigateToSend)
                }

                is EditContactsIntent.GoBack -> {
                    sendEvent(EditContactsEvent.NavigateBack)
                }
            }
        }

    override fun reduceState(
        currentState: EditContactsState,
        partialState: EditContactsState.PartialState
    ): EditContactsState {
        return when (partialState) {
            is EditContactsState.PartialState.ContactsLoaded -> {
                currentState.copy(selectedContacts = partialState.contacts)
            }
        }
    }

    override fun createErrorState(message: String): EditContactsState.PartialState {
        throw NotImplementedError("Error state not implemented")
    }
}
