package xyz.sattar.javid.marketmessage.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import xyz.sattar.javid.marketmessage.data.local.entity.CustomerEntity

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    @Query("SELECT * FROM customers ORDER BY fullName ASC")
    fun getAllCustomers(): PagingSource<Int, CustomerEntity>

    @Query("SELECT * FROM customers WHERE fullName LIKE '%' || :query || '%' OR phoneNumber LIKE '%' || :query || '%' ORDER BY fullName ASC")
    fun searchCustomers(query: String): PagingSource<Int, CustomerEntity>

    @Query("SELECT * FROM customers WHERE phoneNumber = :phoneNumber LIMIT 1")
    suspend fun getCustomerByPhoneNumber(phoneNumber: String): CustomerEntity?
}
