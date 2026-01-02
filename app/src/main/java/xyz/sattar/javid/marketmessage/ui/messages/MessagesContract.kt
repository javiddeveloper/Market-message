package xyz.sattar.javid.marketmessage.ui.messages

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.Customer
import xyz.sattar.javid.marketmessage.domain.model.CustomerStats
import xyz.sattar.javid.marketmessage.domain.model.RecentMessageStat

sealed interface MessagesIntent {
    data object CreateNewMessage : MessagesIntent
    data class UpdateCustomerName(val customer: Customer, val newName: String) : MessagesIntent
    data class DeleteCustomer(val customer: Customer) : MessagesIntent
}

data class MessagesState(
    val customerStats: Flow<PagingData<CustomerStats>>? = null,
    val recentMessages: Flow<PagingData<RecentMessageStat>>? = null,
    val errorMessage: String? = null
) {
    sealed interface PartialState {
        data class Error(val message: String) : MessagesState.PartialState
    }
}

sealed interface MessagesEvent {
    data object NavigateToCreateMessage : MessagesEvent
}
