package xyz.sattar.javid.marketmessage.domain.usecase

import kotlinx.coroutines.flow.first
import xyz.sattar.javid.marketmessage.domain.model.Customer
import xyz.sattar.javid.marketmessage.domain.repository.CustomerRepository
import xyz.sattar.javid.marketmessage.domain.repository.MessageDraftRepository
import javax.inject.Inject

class SaveDraftContactsToCustomersUseCase @Inject constructor(
    private val messageDraftRepository: MessageDraftRepository,
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke() {
        val selectedContacts = messageDraftRepository.getSelectedContacts().first()
        selectedContacts.forEach { draftContact ->
            val existingCustomer = customerRepository.getCustomerByPhoneNumber(draftContact.phoneNumber)
            
            val customer = if (existingCustomer != null) {
                existingCustomer.copy(
                    editFullName = draftContact.name
                    // fullName remains unchanged
                )
            } else {
                Customer(
                    id = 0, // Auto-generated
                    fullName = draftContact.name,
                    editFullName = draftContact.name,
                    phoneNumber = draftContact.phoneNumber,
                    createdAt = System.currentTimeMillis()
                )
            }
            customerRepository.saveCustomer(customer)
        }
    }
}
