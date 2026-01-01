package xyz.sattar.javid.marketmessage.domain.model

data class Message(
    val id: Long,
    val userId: String,
    val receiver: String,
    val messageType: String,
    val content: String,
    val sentAt: Long,
)
