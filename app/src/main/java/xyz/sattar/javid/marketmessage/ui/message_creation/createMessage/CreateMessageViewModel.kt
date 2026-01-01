package xyz.sattar.javid.marketmessage.ui.message_creation.createMessage

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.sattar.javid.marketmessage.domain.model.ReadyMessage
import xyz.sattar.javid.marketmessage.domain.usecase.DeleteReadyMessageUseCase
import xyz.sattar.javid.marketmessage.domain.usecase.GetReadyMessagesUseCase
import xyz.sattar.javid.marketmessage.domain.usecase.InsertReadyMessageUseCase
import xyz.sattar.javid.marketmessage.domain.usecase.UpdateMessageDraftUseCase
import xyz.sattar.javid.marketmessage.ui.components.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class CreateMessageViewModel @Inject constructor(
    private val getReadyMessagesUseCase: GetReadyMessagesUseCase,
    private val deleteReadyMessageUseCase: DeleteReadyMessageUseCase,
    private val insertReadyMessageUseCase: InsertReadyMessageUseCase,
    private val updateMessageDraftUseCase: UpdateMessageDraftUseCase
) : BaseViewModel<CreateMessageState, CreateMessageState.PartialState, CreateMessageEvent, CreateMessageIntent>(
    initialState = CreateMessageState()
) {

    init {
        sendIntent(CreateMessageIntent.LoadReadyMessages)
    }

    override fun handleIntent(intent: CreateMessageIntent): Flow<CreateMessageState.PartialState> =
        flow {
            when (intent) {

                is CreateMessageIntent.LoadReadyMessages -> {
                    emit(CreateMessageState.PartialState.Loading(true))
                    try {
                        // This might suspend until a value is available.
                        // Repo initializes with empty string.
                        getReadyMessagesUseCase().collect {
                            emit(CreateMessageState.PartialState.ReadyMessagesLoaded(it))
                        }
                    } catch (e: Exception) {
                        emit(CreateMessageState.PartialState.Error(e.message ?: "Unknown error"))
                    }

                    try {
                        getReadyMessagesUseCase().collect { messages ->
                            emit(CreateMessageState.PartialState.ReadyMessagesLoaded(messages))
                            emit(CreateMessageState.PartialState.Loading(false))
                        }
                    } catch (e: Exception) {
                        emit(CreateMessageState.PartialState.Error(e.message ?: "Unknown error"))
                        emit(CreateMessageState.PartialState.Loading(false))
                    }
                }

                is CreateMessageIntent.AddReadyMessage -> {
                    try {
                        insertReadyMessageUseCase(ReadyMessage(content = intent.content))
                    } catch (e: Exception) {
                        emit(
                            CreateMessageState.PartialState.Error(
                                e.message ?: "Failed to add message"
                            )
                        )
                    }
                }

                is CreateMessageIntent.DeleteReadyMessage -> {
                    try {
                        deleteReadyMessageUseCase(intent.id)
                    } catch (e: Exception) {
                        emit(
                            CreateMessageState.PartialState.Error(
                                e.message ?: "Failed to delete message"
                            )
                        )
                    }
                }

                is CreateMessageIntent.SelectMessage -> {
                    updateMessageDraftUseCase(intent.id, intent.content)
                    sendEvent(CreateMessageEvent.NavigateToSelectContacts)
                }

                is CreateMessageIntent.GoToSelectContacts -> {
                    sendEvent(CreateMessageEvent.NavigateToSelectContacts)
                }

                is CreateMessageIntent.GoBack -> {
                    sendEvent(CreateMessageEvent.NavigateBack)
                }
            }
        }

    override fun reduceState(
        currentState: CreateMessageState,
        partialState: CreateMessageState.PartialState
    ): CreateMessageState {
        return when (partialState) {
            is CreateMessageState.PartialState.ReadyMessagesLoaded -> currentState.copy(
                isLoading = false, readyMessages = partialState.messages
            )
            is CreateMessageState.PartialState.Loading -> currentState.copy(isLoading = partialState.isLoading)
            is CreateMessageState.PartialState.Error -> currentState.copy(
                isLoading = false,
                error = partialState.message
            )
        }
    }

    override fun createErrorState(message: String): CreateMessageState.PartialState {
        return CreateMessageState.PartialState.Error(message)
    }
}
