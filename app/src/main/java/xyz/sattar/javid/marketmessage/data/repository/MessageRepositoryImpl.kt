package xyz.sattar.javid.marketmessage.data.repository

import xyz.sattar.javid.marketmessage.domain.model.Message
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor() {
    fun getMessages(): List<Message> {
        return emptyList()
    }
}
