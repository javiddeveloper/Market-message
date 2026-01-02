package xyz.sattar.javid.marketmessage.ui.message_creation.selectContacts

import xyz.sattar.javid.marketmessage.domain.model.DeviceContact

enum class SortOrder {
    ASCENDING, DESCENDING
}

sealed interface SelectContactsIntent {
    data object LoadContacts : SelectContactsIntent
    data class SearchContacts(val query: String) : SelectContactsIntent
    data object ToggleSort : SelectContactsIntent
    data class ToggleContactSelection(val contact: DeviceContact) : SelectContactsIntent
    data object GoToEditContacts : SelectContactsIntent
    data object GoBack : SelectContactsIntent
}

data class SelectContactsState(
    val contacts: List<DeviceContact> = emptyList(),
    val filteredContacts: List<DeviceContact> = emptyList(),
    val selectedContacts: List<DeviceContact> = emptyList(),
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.ASCENDING,
    val isLoading: Boolean = false,
    val permissionGranted: Boolean = false,
    val error: String? = null
) {
    sealed interface PartialState {
        data class ContactsLoaded(val contacts: List<DeviceContact>) : PartialState
        data class SearchQueryChanged(val query: String, val filtered: List<DeviceContact>) : PartialState
        data class SortChanged(val sortOrder: SortOrder, val filtered: List<DeviceContact>) : PartialState
        data class SelectionUpdated(val selected: List<DeviceContact>) : PartialState
        data class PermissionStatusChanged(val isGranted: Boolean) : PartialState
        data class Loading(val isLoading: Boolean) : PartialState
        data class Error(val message: String) : PartialState
    }
}

sealed interface SelectContactsEvent {
    data object NavigateToEditContacts : SelectContactsEvent
    data object NavigateBack : SelectContactsEvent
}
