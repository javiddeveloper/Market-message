package xyz.sattar.javid.marketmessage.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.sattar.javid.marketmessage.data.local.dao.CustomerDao
import xyz.sattar.javid.marketmessage.data.local.dao.MessageDao
import xyz.sattar.javid.marketmessage.data.local.entity.MessageEntity
import xyz.sattar.javid.marketmessage.domain.model.Message
import xyz.sattar.javid.marketmessage.domain.repository.MessageRepository
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val customerDao: CustomerDao
) : MessageRepository {

    override suspend fun saveMessage(content: String, contacts: List<String>) {
        val messages = contacts.mapNotNull { contact ->
            val customer = customerDao.getCustomerByPhoneNumber(contact)
            customer?.let {
                MessageEntity(
                    customerId = it.id,
                    messageType = "SMS",
                    content = content,
                    sentAt = System.currentTimeMillis()
                )
            }
        }
        if (messages.isNotEmpty()) {
            messageDao.insertMessages(messages)
        }
    }

    override suspend fun saveSingleMessage(message: Message) {
        messageDao.insertMessage(MessageEntity.fromDomain(message))
    }

    override fun getAllMessages(): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { messageDao.getAllMessagesPaged() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getMessagesForCustomer(customerId: Long): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { messageDao.getMessagesForCustomerPaged(customerId) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getMostFrequentMessageType(): Flow<xyz.sattar.javid.marketmessage.domain.model.MessageTypeStats?> {
        return messageDao.getMostFrequentMessageType().map { daoStats ->
            daoStats?.let {
                xyz.sattar.javid.marketmessage.domain.model.MessageTypeStats(
                    messageType = it.messageType,
                    count = it.count
                )
            }
        }
    }

    override fun getLastMessages(limit: Int): Flow<List<Message>> {
        return messageDao.getLastMessages(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getRecentUniqueMessages(limit: Int): Flow<List<xyz.sattar.javid.marketmessage.domain.model.RecentMessageStat>> {
        return messageDao.getRecentUniqueMessages(limit).map { entities ->
            entities.map { entity ->
                xyz.sattar.javid.marketmessage.domain.model.RecentMessageStat(
                    messageType = entity.messageType,
                    content = entity.content,
                    count = entity.count,
                    lastSent = entity.lastSent
                )
            }
        }
    }

    override fun getTopFrequentContacts(limit: Int): Flow<List<xyz.sattar.javid.marketmessage.domain.model.CustomerStats>> {
        return messageDao.getTopFrequentContacts(limit).map { entities ->
            entities.map { entity ->
                xyz.sattar.javid.marketmessage.domain.model.CustomerStats(
                    customer = entity.customer.toDomain(),
                    messageCount = entity.messageCount
                )
            }
        }
    }
}
