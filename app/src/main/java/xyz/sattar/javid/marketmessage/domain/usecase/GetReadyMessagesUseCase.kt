package xyz.sattar.javid.marketmessage.domain.usecase

import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.ReadyMessage
import xyz.sattar.javid.marketmessage.domain.repository.ReadyMessageRepository
import javax.inject.Inject

class GetReadyMessagesUseCase @Inject constructor(
    private val readyMessageRepository: ReadyMessageRepository
) {
    operator fun invoke(): Flow<List<ReadyMessage>> {
        return readyMessageRepository.getAllReadyMessages()
    }
}
