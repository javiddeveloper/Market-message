package xyz.sattar.javid.marketmessage.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.Message
import xyz.sattar.javid.marketmessage.domain.repository.MessageRepository
import javax.inject.Inject

class GetMessagesForCustomerUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(customerId: String): Flow<PagingData<Message>> {
        return messageRepository.getMessagesForCustomer(customerId)
    }
}
