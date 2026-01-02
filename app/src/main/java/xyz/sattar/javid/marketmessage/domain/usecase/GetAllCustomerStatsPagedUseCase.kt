package xyz.sattar.javid.marketmessage.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.CustomerStats
import xyz.sattar.javid.marketmessage.domain.repository.MessageRepository
import javax.inject.Inject

class GetAllCustomerStatsPagedUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(): Flow<PagingData<CustomerStats>> {
        return messageRepository.getAllCustomerStatsPaged()
    }
}
