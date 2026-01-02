package xyz.sattar.javid.marketmessage.domain.usecase

import xyz.sattar.javid.marketmessage.domain.model.Customer
import xyz.sattar.javid.marketmessage.domain.repository.CustomerRepository
import javax.inject.Inject

class GetCustomerByPhoneNumberUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(phoneNumber: String): Customer? {
        return customerRepository.getCustomerByPhoneNumber(phoneNumber)
    }
}
