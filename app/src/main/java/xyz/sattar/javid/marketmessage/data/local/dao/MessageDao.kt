package xyz.sattar.javid.marketmessage.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.data.local.entity.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("SELECT * FROM messages ORDER BY sentAt DESC")
    fun getAllMessagesPaged(): PagingSource<Int, MessageEntity>

    @Query("SELECT * FROM messages WHERE customerId = :customerId ORDER BY sentAt DESC")
    fun getMessagesForCustomerPaged(customerId: Long): PagingSource<Int, MessageEntity>

    @Query("DELETE FROM messages")
    suspend fun clearAllMessages()

    @Query("SELECT messageType, COUNT(*) as count FROM messages GROUP BY messageType ORDER BY count DESC LIMIT 1")
    fun getMostFrequentMessageType(): Flow<xyz.sattar.javid.marketmessage.data.local.model.MessageTypeCount?>

    @Query("SELECT * FROM messages ORDER BY sentAt DESC LIMIT :limit")
    fun getLastMessages(limit: Int): Flow<List<MessageEntity>>

    @Query("SELECT messageType, content, COUNT(*) as count, MAX(sentAt) as lastSent FROM messages GROUP BY messageType, content ORDER BY lastSent DESC LIMIT :limit")
    fun getRecentUniqueMessages(limit: Int): Flow<List<xyz.sattar.javid.marketmessage.data.local.model.RecentMessageCount>>

    @Query("SELECT c.*, COUNT(m.id) as messageCount FROM customers c JOIN messages m ON c.id = m.customerId GROUP BY c.id ORDER BY messageCount DESC LIMIT :limit")
    fun getTopFrequentContacts(limit: Int): Flow<List<xyz.sattar.javid.marketmessage.data.local.model.CustomerMessageCount>>
}
