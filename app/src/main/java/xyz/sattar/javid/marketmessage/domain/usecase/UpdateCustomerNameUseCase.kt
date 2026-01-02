package xyz.sattar.javid.marketmessage.domain.usecase

import xyz.sattar.javid.marketmessage.domain.model.Customer
import xyz.sattar.javid.marketmessage.domain.repository.CustomerRepository
import javax.inject.Inject

class UpdateCustomerNameUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(customer: Customer, newName: String) {
        val updatedCustomer = customer.copy(editFullName = newName)
        customerRepository.saveCustomer(updatedCustomer) // Assuming saveCustomer handles update (OnConflictStrategy.REPLACE)
    }
}
