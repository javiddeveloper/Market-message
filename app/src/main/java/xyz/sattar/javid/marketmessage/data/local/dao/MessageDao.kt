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

    @Query("SELECT * FROM messages WHERE receiver = :customerId ORDER BY sentAt DESC")
    fun getMessagesForCustomerPaged(customerId: String): PagingSource<Int, MessageEntity>

    @Query("DELETE FROM messages")
    suspend fun clearAllMessages()
}
