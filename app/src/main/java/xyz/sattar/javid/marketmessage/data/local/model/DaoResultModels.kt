package xyz.sattar.javid.marketmessage.data.local.model

import androidx.room.Embedded
import xyz.sattar.javid.marketmessage.data.local.entity.CustomerEntity

data class MessageTypeCount(
    val messageType: String,
    val count: Int
)

data class CustomerMessageCount(
    @Embedded val customer: CustomerEntity,
    val messageCount: Int
)
