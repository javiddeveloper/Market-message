package xyz.sattar.javid.marketmessage.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.sattar.javid.marketmessage.domain.model.Message

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerId: Long,
    val messageType: String,
    val content: String,
    val sentAt: Long
) {
    fun toDomain(): Message {
        return Message(
            id = id,
            customerId = customerId,
            messageType = messageType,
            content = content,
            sentAt = sentAt
        )
    }

    companion object {
        fun fromDomain(message: Message): MessageEntity {
            return MessageEntity(
                id = message.id,
                customerId = message.customerId,
                messageType = message.messageType,
                content = message.content,
                sentAt = message.sentAt
            )
        }
    }
}
