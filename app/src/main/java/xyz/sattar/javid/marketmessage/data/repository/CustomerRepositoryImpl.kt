package xyz.sattar.javid.marketmessage.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.sattar.javid.marketmessage.data.local.dao.CustomerDao
import xyz.sattar.javid.marketmessage.data.local.entity.CustomerEntity
import xyz.sattar.javid.marketmessage.domain.model.Customer
import xyz.sattar.javid.marketmessage.domain.repository.CustomerRepository
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val customerDao: CustomerDao
) : CustomerRepository {

    override fun getAllCustomers(): Flow<PagingData<Customer>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { customerDao.getAllCustomers() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun searchCustomers(query: String): Flow<PagingData<Customer>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { customerDao.searchCustomers(query) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun saveCustomer(customer: Customer) {
        customerDao.insertCustomer(CustomerEntity.fromDomain(customer))
    }

    override suspend fun getCustomerByPhoneNumber(phoneNumber: String): Customer? {
        return customerDao.getCustomerByPhoneNumber(phoneNumber)?.toDomain()
    }

    override suspend fun deleteCustomer(customer: Customer) {
        customerDao.deleteCustomer(CustomerEntity.fromDomain(customer))
    }
}
