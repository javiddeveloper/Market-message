package xyz.sattar.javid.marketmessage.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.sattar.javid.marketmessage.data.local.dao.ReadyMessageDao
import xyz.sattar.javid.marketmessage.data.local.entity.ReadyMessageEntity
import xyz.sattar.javid.marketmessage.domain.model.ReadyMessage
import xyz.sattar.javid.marketmessage.domain.repository.ReadyMessageRepository
import javax.inject.Inject

class ReadyMessageRepositoryImpl @Inject constructor(
    private val readyMessageDao: ReadyMessageDao
) : ReadyMessageRepository {

    override fun getAllReadyMessages(): Flow<List<ReadyMessage>> {
        return readyMessageDao.getAllReadyMessages().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertReadyMessage(readyMessage: ReadyMessage) {
        readyMessageDao.insertReadyMessage(ReadyMessageEntity.fromDomain(readyMessage))
    }

    override suspend fun deleteReadyMessage(id: Long) {
        readyMessageDao.deleteReadyMessage(id)
    }
}
