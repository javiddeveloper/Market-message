package xyz.sattar.javid.marketmessage.domain.repository

import kotlinx.coroutines.flow.Flow

interface MessageDraftRepository {
    fun getMessageBody(): Flow<String>
    suspend fun updateMessageBody(body: String)
    
    fun getMessageId(): Flow<Long?>
    suspend fun updateMessageId(id: Long?)

    fun getSelectedContacts(): Flow<List<String>>
    suspend fun toggleContactSelection(contact: String)
    suspend fun clearDraft()
}
