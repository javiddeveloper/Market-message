package xyz.sattar.javid.marketmessage.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.RecentMessageStat
import xyz.sattar.javid.marketmessage.domain.repository.MessageRepository
import javax.inject.Inject

class GetRecentUniqueMessagesPagedUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(): Flow<PagingData<RecentMessageStat>> {
        return messageRepository.getRecentUniqueMessagesPaged()
    }
}
