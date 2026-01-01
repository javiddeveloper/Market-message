package xyz.sattar.javid.marketmessage.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.Customer

interface CustomerRepository {
    fun getAllCustomers(): Flow<PagingData<Customer>>
    fun searchCustomers(query: String): Flow<PagingData<Customer>>
}
