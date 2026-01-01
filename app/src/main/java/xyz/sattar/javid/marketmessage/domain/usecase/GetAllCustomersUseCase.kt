package xyz.sattar.javid.marketmessage.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.Customer
import xyz.sattar.javid.marketmessage.domain.repository.CustomerRepository
import javax.inject.Inject

class GetAllCustomersUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    operator fun invoke(): Flow<PagingData<Customer>> {
        return customerRepository.getAllCustomers()
    }
}
