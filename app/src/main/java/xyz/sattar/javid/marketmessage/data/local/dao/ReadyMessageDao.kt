package xyz.sattar.javid.marketmessage.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.data.local.entity.ReadyMessageEntity

@Dao
interface ReadyMessageDao {
    @Query("SELECT * FROM ready_messages ORDER BY id DESC")
    fun getAllReadyMessages(): Flow<List<ReadyMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadyMessage(readyMessage: ReadyMessageEntity)

    @Query("DELETE FROM ready_messages WHERE id = :id")
    suspend fun deleteReadyMessage(id: Long)
}
