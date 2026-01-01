package xyz.sattar.javid.marketmessage.ui.message_creation.sendMessage

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import xyz.sattar.javid.marketmessage.domain.repository.MessageDraftRepository
import xyz.sattar.javid.marketmessage.ui.components.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SendMessageViewModel @Inject constructor(
    private val messageDraftRepository: MessageDraftRepository
) : BaseViewModel<SendMessageState, SendMessageState.PartialState, SendMessageEvent, SendMessageIntent>(
    initialState = SendMessageState()
) {

    init {
        sendIntent(SendMessageIntent.LoadDraft)
    }

    override fun handleIntent(intent: SendMessageIntent): Flow<SendMessageState.PartialState> =
        flow {
            when (intent) {
                is SendMessageIntent.LoadDraft -> {
                    combine(
                        messageDraftRepository.getMessageBody(),
                        messageDraftRepository.getSelectedContacts()
                    ) { body, contacts ->
                        SendMessageState.PartialState.DraftLoaded(body, contacts.size)
                    }.collect { partialState ->
                        emit(partialState)
                    }
                }

                is SendMessageIntent.SendMessage -> {
                    emit(SendMessageState.PartialState.Sending(true))
                    try {
                        // Simulate sending or call actual UseCase
                        // For now just clear draft
                        messageDraftRepository.clearDraft()
                        emit(SendMessageState.PartialState.Sent) // Logic to handle "Sent" state? 
                        // Actually BaseViewModel reducer should handle it or we emit event?
                        // If we emit Sent partial state, reducer updates state. 
                        // But we want to navigate away.
                        sendEvent(SendMessageEvent.MessageSent)
                    } catch (e: Exception) {
                        emit(SendMessageState.PartialState.Error(e.message ?: "Failed to send"))
                    } finally {
                        emit(SendMessageState.PartialState.Sending(false))
                    }
                }

                is SendMessageIntent.GoBack -> {
                    sendEvent(SendMessageEvent.NavigateBack)
                }
            }
        }

    override fun reduceState(
        currentState: SendMessageState,
        partialState: SendMessageState.PartialState
    ): SendMessageState {
        return when (partialState) {
            is SendMessageState.PartialState.DraftLoaded -> currentState.copy(
                messageBody = partialState.body,
                contactCount = partialState.contactCount
            )
            is SendMessageState.PartialState.Sending -> currentState.copy(isSending = partialState.isSending)
            is SendMessageState.PartialState.Error -> currentState.copy(error = partialState.message)
            is SendMessageState.PartialState.Sent -> currentState // Or reset?
        }
    }

    override fun createErrorState(message: String): SendMessageState.PartialState {
        return SendMessageState.PartialState.Error(message)
    }
}
