package xyz.sattar.javid.marketmessage.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.Customer
import xyz.sattar.javid.marketmessage.domain.model.CustomerStats
import xyz.sattar.javid.marketmessage.domain.model.Message
import xyz.sattar.javid.marketmessage.domain.model.MessageTypeStats
import xyz.sattar.javid.marketmessage.domain.model.RecentMessageStat

interface MessageRepository {
    suspend fun saveMessage(content: String, contacts: List<String>)
    suspend fun saveSingleMessage(message: Message)
    fun getAllMessages(): Flow<PagingData<Message>>
    fun getMessagesForCustomer(customerId: Long): Flow<PagingData<Message>>
    fun getMostFrequentMessageType(): Flow<MessageTypeStats?>
    fun getLastMessages(limit: Int): Flow<List<Message>>
    fun getRecentUniqueMessages(limit: Int): Flow<List<RecentMessageStat>>
    fun getTopFrequentContacts(limit: Int): Flow<List<CustomerStats>>
    
    fun getAllCustomerStatsPaged(): Flow<PagingData<CustomerStats>>
    fun getRecentUniqueMessagesPaged(): Flow<PagingData<RecentMessageStat>>
    suspend fun deleteMessagesForCustomer(customerId: Long)
}
