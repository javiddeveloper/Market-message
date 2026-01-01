package xyz.sattar.javid.marketmessage.domain.usecase

import xyz.sattar.javid.marketmessage.domain.repository.ReadyMessageRepository
import javax.inject.Inject

class DeleteReadyMessageUseCase @Inject constructor(
    private val readyMessageRepository: ReadyMessageRepository
) {
    suspend operator fun invoke(id: Long) {
        readyMessageRepository.deleteReadyMessage(id)
    }
}
