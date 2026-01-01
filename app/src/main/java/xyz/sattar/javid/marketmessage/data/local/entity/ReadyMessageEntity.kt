package xyz.sattar.javid.marketmessage.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.sattar.javid.marketmessage.domain.model.ReadyMessage

@Entity(tableName = "ready_messages")
data class ReadyMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String
) {
    fun toDomain(): ReadyMessage {
        return ReadyMessage(
            id = id,
            content = content
        )
    }

    companion object {
        fun fromDomain(readyMessage: ReadyMessage): ReadyMessageEntity {
            return ReadyMessageEntity(
                id = readyMessage.id,
                content = readyMessage.content
            )
        }
    }
}
