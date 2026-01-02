package xyz.sattar.javid.marketmessage.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import xyz.sattar.javid.marketmessage.domain.model.HomeStatistics
import xyz.sattar.javid.marketmessage.domain.repository.MessageRepository
import javax.inject.Inject

class GetHomeStatsUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(): Flow<HomeStatistics> {
        return combine(
            messageRepository.getMostFrequentMessageType(),
            messageRepository.getTopFrequentContacts(5),
            messageRepository.getRecentUniqueMessages(5)
        ) { typeStats, topContacts, lastMessages ->
            HomeStatistics(
                mostFrequentMessageType = typeStats,
                topFrequentContacts = topContacts,
                recentMessages = lastMessages
            )
        }
    }
}
