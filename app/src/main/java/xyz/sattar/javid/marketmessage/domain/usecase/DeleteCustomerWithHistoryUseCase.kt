package xyz.sattar.javid.marketmessage.domain.usecase

import xyz.sattar.javid.marketmessage.domain.model.Customer
import xyz.sattar.javid.marketmessage.domain.repository.CustomerRepository
import xyz.sattar.javid.marketmessage.domain.repository.MessageRepository
import javax.inject.Inject

class DeleteCustomerWithHistoryUseCase @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(customer: Customer) {
        // Delete all messages for this customer
        messageRepository.deleteMessagesForCustomer(customer.id)
        
        // Delete the customer
        customerRepository.deleteCustomer(customer)
    }
}
