package xyz.sattar.javid.marketmessage.domain.usecase

import xyz.sattar.javid.marketmessage.domain.repository.MessageDraftRepository
import javax.inject.Inject

class UpdateMessageDraftUseCase @Inject constructor(
    private val repository: MessageDraftRepository
) {
    suspend operator fun invoke(id: Long, content: String) {
        repository.updateMessageId(id)
        repository.updateMessageBody(content)
    }
}
