package xyz.sattar.javid.marketmessage.ui.message_creation.sendMessage

import android.content.Context
import android.telephony.SmsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import xyz.sattar.javid.marketmessage.domain.model.MessageVariable
import xyz.sattar.javid.marketmessage.domain.repository.MessageDraftRepository
import xyz.sattar.javid.marketmessage.domain.usecase.SaveDraftContactsToCustomersUseCase
import xyz.sattar.javid.marketmessage.domain.usecase.SaveSentMessageUseCase
import xyz.sattar.javid.marketmessage.ui.components.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SendMessageViewModel @Inject constructor(
    private val messageDraftRepository: MessageDraftRepository,
    private val saveSentMessageUseCase: SaveSentMessageUseCase,
    private val saveDraftContactsToCustomersUseCase: SaveDraftContactsToCustomersUseCase,
    @ApplicationContext private val context: Context
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
                    try {
                        saveDraftContactsToCustomersUseCase()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    emitAll(
                        combine(
                            messageDraftRepository.getMessageBody(),
                            messageDraftRepository.getSelectedContacts()
                        ) { body, contacts ->
                            SendMessageState.PartialState.DraftLoaded(body, contacts)
                        }
                    )
                }

                is SendMessageIntent.SendSingleSms -> {
                    if (uiState.value.messageBody.isBlank()) {
                        emit(SendMessageState.PartialState.Error("متن پیام نمی‌تواند خالی باشد"))
                        return@flow
                    }
                    try {
                        val smsManager = try {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                context.getSystemService(android.telephony.SmsManager::class.java)
                            } else {
                                SmsManager.getDefault()
                            }
                        } catch (e: Exception) {
                            SmsManager.getDefault()
                        }

                        if (smsManager != null) {
                            val personalizedBody = MessageVariable.replaceVariables(uiState.value.messageBody) { variable ->
                                when (variable) {
                                    MessageVariable.NAME -> intent.contact.name
                                }
                            }
                            smsManager.sendTextMessage(
                                intent.contact.phoneNumber,
                                null,
                                personalizedBody,
                                null,
                                null
                            )
                            saveSentMessageUseCase(personalizedBody, intent.contact.phoneNumber)
                            emit(SendMessageState.PartialState.SmsSent(intent.contact.phoneNumber))
                        } else {
                            emit(SendMessageState.PartialState.Error("SMS Manager not available"))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        emit(SendMessageState.PartialState.Error(e.message ?: "Failed to send SMS"))
                    }
                }

                is SendMessageIntent.SendBulkSms -> {
                    if (uiState.value.messageBody.isBlank()) {
                        emit(SendMessageState.PartialState.Error("متن پیام نمی‌تواند خالی باشد"))
                        return@flow
                    }
                    emit(SendMessageState.PartialState.Sending(true))
                    try {
                        val smsManager = try {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                context.getSystemService(android.telephony.SmsManager::class.java)
                            } else {
                                SmsManager.getDefault()
                            }
                        } catch (e: Exception) {
                            SmsManager.getDefault()
                        }

                        if (smsManager != null) {
                            val contactsToSend = uiState.value.contacts.filter { 
                                !uiState.value.sentPhoneNumbers.contains(it.phoneNumber) 
                            }
                            
                            contactsToSend.forEach { contact ->
                                try {
                                    delay(500) // Add delay to prevent rate limiting
                                    val personalizedBody = MessageVariable.replaceVariables(uiState.value.messageBody) { variable ->
                                        when (variable) {
                                            MessageVariable.NAME -> contact.name
                                        }
                                    }
                                    smsManager.sendTextMessage(
                                        contact.phoneNumber,
                                        null,
                                        personalizedBody,
                                        null,
                                        null
                                    )
                                    saveSentMessageUseCase(personalizedBody, contact.phoneNumber)
                                    emit(SendMessageState.PartialState.SmsSent(contact.phoneNumber))
                                } catch (e: Exception) {
                                    // Continue sending to other contacts even if one fails
                                    e.printStackTrace()
                                }
                            }
                        } else {
                            emit(SendMessageState.PartialState.Error("SMS Manager not available"))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        emit(SendMessageState.PartialState.Error(e.message ?: "Failed to send bulk SMS"))
                    } finally {
                        emit(SendMessageState.PartialState.Sending(false))
                    }
                }

                is SendMessageIntent.GotoDashboard -> {
                    messageDraftRepository.clearDraft()
                    sendEvent(SendMessageEvent.NavigateDashboard)
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
                contacts = partialState.contacts
            )

            is SendMessageState.PartialState.SmsSent -> currentState.copy(
                sentPhoneNumbers = currentState.sentPhoneNumbers + partialState.phoneNumber
            )

            is SendMessageState.PartialState.Sending -> currentState.copy(isSending = partialState.isSending)
            is SendMessageState.PartialState.Error -> currentState.copy(error = partialState.message)
        }
    }

    override fun createErrorState(message: String): SendMessageState.PartialState {
        return SendMessageState.PartialState.Error(message)
    }
}
