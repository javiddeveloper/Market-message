package xyz.sattar.javid.marketmessage.ui.message_creation.selectContacts

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.sattar.javid.marketmessage.domain.model.DeviceContact
import xyz.sattar.javid.marketmessage.domain.model.DraftContact
import xyz.sattar.javid.marketmessage.domain.repository.CustomerRepository
import xyz.sattar.javid.marketmessage.domain.repository.DeviceContactRepository
import xyz.sattar.javid.marketmessage.domain.repository.MessageDraftRepository
import xyz.sattar.javid.marketmessage.domain.usecase.GetCustomerByPhoneNumberUseCase
import xyz.sattar.javid.marketmessage.ui.components.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SelectContactsViewModel @Inject constructor(
    private val deviceContactRepository: DeviceContactRepository,
    private val getCustomerByPhoneNumberUseCase: GetCustomerByPhoneNumberUseCase,
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
                                filterAndSort(contacts, uiState.value.searchQuery, uiState.value.sortOrder)
                            )
                        )
                        emit(SelectContactsState.PartialState.Loading(false))
                    }
                }

                is SelectContactsIntent.SearchContacts -> {
                    emit(
                        SelectContactsState.PartialState.SearchQueryChanged(
                            intent.query,
                            filterAndSort(uiState.value.contacts, intent.query, uiState.value.sortOrder)
                        )
                    )
                }

                is SelectContactsIntent.ToggleSort -> {
                    val newSortOrder = if (uiState.value.sortOrder == SortOrder.ASCENDING) {
                        SortOrder.DESCENDING
                    } else {
                        SortOrder.ASCENDING
                    }
                    emit(
                        SelectContactsState.PartialState.SortChanged(
                            newSortOrder,
                            filterAndSort(uiState.value.contacts, uiState.value.searchQuery, newSortOrder)
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
                    val existingCustomer = getCustomerByPhoneNumberUseCase(intent.contact.phoneNumber)
                    val contactName = existingCustomer?.editFullName ?: intent.contact.name

                    messageDraftRepository.toggleContactSelection(
                        DraftContact(
                            name = contactName,
                            phoneNumber = intent.contact.phoneNumber
                        )
                    )
                    
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
        sortOrder: SortOrder
    ): List<DeviceContact> {
        val filtered = if (query.isBlank()) {
            contacts
        } else {
            contacts.filter {
                it.name.contains(query, ignoreCase = true) || it.phoneNumber.contains(query)
            }
        }

        return if (sortOrder == SortOrder.ASCENDING) {
            filtered.sortedBy { it.name }
        } else {
            filtered.sortedByDescending { it.name }
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
                sortOrder = partialState.sortOrder,
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
