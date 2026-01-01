package xyz.sattar.javid.marketmessage.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.sattar.javid.marketmessage.data.local.dao.MessageDao
import xyz.sattar.javid.marketmessage.data.local.entity.MessageEntity
import xyz.sattar.javid.marketmessage.domain.model.Message
import xyz.sattar.javid.marketmessage.domain.repository.MessageRepository
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageRepository {

    override suspend fun saveMessage(content: String, contacts: List<String>) {
        val messages = contacts.map { contact ->
            MessageEntity(
                userId = "current_user", // Placeholder
                receiver = contact,
                messageType = "SMS",
                content = content,
                sentAt = System.currentTimeMillis()
            )
        }
        messageDao.insertMessages(messages)
    }

    override fun getAllMessages(): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { messageDao.getAllMessagesPaged() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getMessagesForCustomer(customerId: String): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { messageDao.getMessagesForCustomerPaged(customerId) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }
}
