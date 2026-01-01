package xyz.sattar.javid.marketmessage.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.Message

interface MessageRepository {
    suspend fun saveMessage(content: String, contacts: List<String>)
    fun getAllMessages(): Flow<PagingData<Message>>
    fun getMessagesForCustomer(customerId: String): Flow<PagingData<Message>>
}
