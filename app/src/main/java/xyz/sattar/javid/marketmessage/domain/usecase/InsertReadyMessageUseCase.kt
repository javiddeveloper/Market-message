package xyz.sattar.javid.marketmessage.domain.usecase

import xyz.sattar.javid.marketmessage.domain.model.ReadyMessage
import xyz.sattar.javid.marketmessage.domain.repository.ReadyMessageRepository
import javax.inject.Inject

class InsertReadyMessageUseCase @Inject constructor(
    private val readyMessageRepository: ReadyMessageRepository
) {
    suspend operator fun invoke(readyMessage: ReadyMessage) {
        readyMessageRepository.insertReadyMessage(readyMessage)
    }
}
