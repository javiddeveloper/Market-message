package xyz.sattar.javid.marketmessage.data.repository

import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.data.local.MessageDraftDataHolder
import xyz.sattar.javid.marketmessage.domain.repository.MessageDraftRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageDraftRepositoryImpl @Inject constructor() : MessageDraftRepository {

    override fun getMessageBody(): Flow<String> = MessageDraftDataHolder.messageBody

    override suspend fun updateMessageBody(body: String) {
        MessageDraftDataHolder.updateMessageBody(body)
    }

    override fun getMessageId(): Flow<Long?> = MessageDraftDataHolder.messageId

    override suspend fun updateMessageId(id: Long?) {
        MessageDraftDataHolder.updateMessageId(id)
    }

    override fun getSelectedContacts(): Flow<List<String>> = MessageDraftDataHolder.selectedContacts

    override suspend fun toggleContactSelection(contact: String) {
        MessageDraftDataHolder.toggleContactSelection(contact)
    }

    override suspend fun clearDraft() {
        MessageDraftDataHolder.clearDraft()
    }
}
