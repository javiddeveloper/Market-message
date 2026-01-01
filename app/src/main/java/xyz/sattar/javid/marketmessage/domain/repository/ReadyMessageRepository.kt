package xyz.sattar.javid.marketmessage.domain.repository

import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.ReadyMessage

interface ReadyMessageRepository {
    fun getAllReadyMessages(): Flow<List<ReadyMessage>>
    suspend fun insertReadyMessage(readyMessage: ReadyMessage)
    suspend fun deleteReadyMessage(id: Long)
}
