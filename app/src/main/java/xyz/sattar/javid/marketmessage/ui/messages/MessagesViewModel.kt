package xyz.sattar.javid.marketmessage.ui.messages

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.sattar.javid.marketmessage.domain.usecase.DeleteCustomerWithHistoryUseCase
import xyz.sattar.javid.marketmessage.domain.usecase.GetAllCustomerStatsPagedUseCase
import xyz.sattar.javid.marketmessage.domain.usecase.GetRecentUniqueMessagesPagedUseCase
import xyz.sattar.javid.marketmessage.domain.usecase.UpdateCustomerNameUseCase
import xyz.sattar.javid.marketmessage.ui.components.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    getAllCustomerStatsPagedUseCase: GetAllCustomerStatsPagedUseCase,
    getRecentUniqueMessagesPagedUseCase: GetRecentUniqueMessagesPagedUseCase,
    private val updateCustomerNameUseCase: UpdateCustomerNameUseCase,
    private val deleteCustomerWithHistoryUseCase: DeleteCustomerWithHistoryUseCase
) : BaseViewModel<MessagesState, MessagesState.PartialState, MessagesEvent, MessagesIntent>(
    initialState = MessagesState(
        customerStats = getAllCustomerStatsPagedUseCase(),
        recentMessages = getRecentUniqueMessagesPagedUseCase()
    )
) {

    override fun handleIntent(intent: MessagesIntent): Flow<MessagesState.PartialState> =
        flow {
            when (intent) {
                is MessagesIntent.CreateNewMessage -> {
                    sendEvent(MessagesEvent.NavigateToCreateMessage)
                }

                is MessagesIntent.UpdateCustomerName -> {
                    updateCustomerNameUseCase(intent.customer, intent.newName)
                    // No state update needed as PagingData should observe DB changes automatically
                }

                is MessagesIntent.DeleteCustomer -> {
                    deleteCustomerWithHistoryUseCase(intent.customer)
                    // No state update needed as PagingData should observe DB changes automatically
                }
            }
        }

    override fun reduceState(
        currentState: MessagesState,
        partialState: MessagesState.PartialState
    ): MessagesState {
        return currentState
    }

    override fun createErrorState(message: String) = MessagesState.PartialState.Error(message)
}
