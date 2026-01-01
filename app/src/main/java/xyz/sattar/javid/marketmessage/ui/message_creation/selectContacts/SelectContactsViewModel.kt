package xyz.sattar.javid.marketmessage.ui.message_creation.selectContacts

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import xyz.sattar.javid.marketmessage.domain.model.DeviceContact
import xyz.sattar.javid.marketmessage.domain.repository.DeviceContactRepository
import xyz.sattar.javid.marketmessage.domain.repository.MessageDraftRepository
import xyz.sattar.javid.marketmessage.ui.components.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SelectContactsViewModel @Inject constructor(
    private val deviceContactRepository: DeviceContactRepository,
    private val messageDraftRepository: MessageDraftRepository
) : BaseViewModel<SelectContactsState, SelectContactsState.PartialState, SelectContactsEvent, SelectContactsIntent>(
    initialState = SelectContactsState()
) {

    init {
        // Initial load happens when permission is granted
    }

    override fun handleIntent(intent: SelectContactsIntent): Flow<SelectContactsState.PartialState> =
        flow {
            when (intent) {
                is SelectContactsIntent.LoadContacts -> {
                    emit(SelectContactsState.PartialState.Loading(true))
                    emit(SelectContactsState.PartialState.PermissionStatusChanged(true))
                    deviceContactRepository.getDeviceContacts().collect { contacts ->
                        emit(SelectContactsState.PartialState.ContactsLoaded(contacts))
                        // Apply initial filter/sort if needed
                        emit(
                            SelectContactsState.PartialState.SearchQueryChanged(
                                uiState.value.searchQuery,
                                filterAndSort(contacts, uiState.value.searchQuery, uiState.value.isSortedByName)
                            )
                        )
                        emit(SelectContactsState.PartialState.Loading(false))
                    }
                }

                is SelectContactsIntent.SearchContacts -> {
                    emit(
                        SelectContactsState.PartialState.SearchQueryChanged(
                            intent.query,
                            filterAndSort(uiState.value.contacts, intent.query, uiState.value.isSortedByName)
                        )
                    )
                }

                is SelectContactsIntent.ToggleSort -> {
                    emit(
                        SelectContactsState.PartialState.SortChanged(
                            intent.sortByName,
                            filterAndSort(uiState.value.contacts, uiState.value.searchQuery, intent.sortByName)
                        )
                    )
                }

                is SelectContactsIntent.ToggleContactSelection -> {
                    val currentSelected = uiState.value.selectedContacts.toMutableList()
                    if (currentSelected.contains(intent.contact)) {
                        currentSelected.remove(intent.contact)
                    } else {
                        currentSelected.add(intent.contact)
                    }
                    
                    // Update repository
                    messageDraftRepository.toggleContactSelection(intent.contact.phoneNumber)
                    
                    emit(SelectContactsState.PartialState.SelectionUpdated(currentSelected))
                }

                is SelectContactsIntent.GoToEditContacts -> {
                    sendEvent(SelectContactsEvent.NavigateToEditContacts)
                }

                is SelectContactsIntent.GoBack -> {
                    sendEvent(SelectContactsEvent.NavigateBack)
                }
            }
        }

    private fun filterAndSort(
        contacts: List<DeviceContact>,
        query: String,
        sortByName: Boolean
    ): List<DeviceContact> {
        val filtered = if (query.isBlank()) {
            contacts
        } else {
            contacts.filter {
                it.name.contains(query, ignoreCase = true) || it.phoneNumber.contains(query)
            }
        }

        return if (sortByName) {
            filtered.sortedBy { it.name }
        } else {
            filtered.sortedBy { it.phoneNumber }
        }
    }

    override fun reduceState(
        currentState: SelectContactsState,
        partialState: SelectContactsState.PartialState
    ): SelectContactsState {
        return when (partialState) {
            is SelectContactsState.PartialState.ContactsLoaded -> currentState.copy(
                contacts = partialState.contacts
            )
            is SelectContactsState.PartialState.SearchQueryChanged -> currentState.copy(
                searchQuery = partialState.query,
                filteredContacts = partialState.filtered
            )
            is SelectContactsState.PartialState.SortChanged -> currentState.copy(
                isSortedByName = partialState.isSortedByName,
                filteredContacts = partialState.filtered
            )
            is SelectContactsState.PartialState.SelectionUpdated -> currentState.copy(
                selectedContacts = partialState.selected
            )
            is SelectContactsState.PartialState.PermissionStatusChanged -> currentState.copy(
                permissionGranted = partialState.isGranted
            )
            is SelectContactsState.PartialState.Loading -> currentState.copy(
                isLoading = partialState.isLoading
            )
            is SelectContactsState.PartialState.Error -> currentState.copy(
                error = partialState.message,
                isLoading = false
            )
        }
    }

    override fun createErrorState(message: String): SelectContactsState.PartialState {
        return SelectContactsState.PartialState.Error(message)
    }
}
