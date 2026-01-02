package xyz.sattar.javid.marketmessage.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.Message

interface MessageRepository {
    suspend fun saveMessage(content: String, contacts: List<String>)
    suspend fun saveSingleMessage(message: Message)
    fun getAllMessages(): Flow<PagingData<Message>>
    fun getMessagesForCustomer(customerId: Long): Flow<PagingData<Message>>
    fun getMostFrequentMessageType(): Flow<xyz.sattar.javid.marketmessage.domain.model.MessageTypeStats?>
    fun getLastMessages(limit: Int): Flow<List<Message>>
    fun getRecentUniqueMessages(limit: Int): Flow<List<xyz.sattar.javid.marketmessage.domain.model.RecentMessageStat>>
    fun getTopFrequentContacts(limit: Int): Flow<List<xyz.sattar.javid.marketmessage.domain.model.CustomerStats>>
}
