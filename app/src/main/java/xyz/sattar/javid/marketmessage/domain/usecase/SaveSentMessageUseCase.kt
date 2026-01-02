package xyz.sattar.javid.marketmessage.domain.usecase

import xyz.sattar.javid.marketmessage.domain.model.Message
import xyz.sattar.javid.marketmessage.domain.repository.CustomerRepository
import xyz.sattar.javid.marketmessage.domain.repository.MessageRepository
import javax.inject.Inject

class SaveSentMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(content: String, phoneNumber: String) {
        val customer = customerRepository.getCustomerByPhoneNumber(phoneNumber)
        if (customer != null) {
            val message = Message(
                id = 0, // Auto-generated
                customerId = customer.id,
                messageType = "SMS",
                content = content,
                sentAt = System.currentTimeMillis()
            )
            messageRepository.saveSingleMessage(message)
        }
    }
}
