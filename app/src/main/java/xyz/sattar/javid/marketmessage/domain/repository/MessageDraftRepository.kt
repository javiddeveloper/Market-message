package xyz.sattar.javid.marketmessage.domain.repository

import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.DraftContact

interface MessageDraftRepository {
    fun getMessageBody(): Flow<String>
    suspend fun updateMessageBody(body: String)
    
    fun getMessageId(): Flow<Long?>
    suspend fun updateMessageId(id: Long?)

    fun getSelectedContacts(): Flow<List<DraftContact>>
    suspend fun toggleContactSelection(contact: DraftContact)
    suspend fun updateContactName(phoneNumber: String, newName: String)
    suspend fun clearDraft()
}
