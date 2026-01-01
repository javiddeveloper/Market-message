package xyz.sattar.javid.marketmessage.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.sattar.javid.marketmessage.domain.model.Customer

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val editFullName: String,
    val phoneNumber: String,
    val createdAt: Long
) {
    fun toDomain(): Customer {
        return Customer(
            id = id,
            fullName = fullName,
            editFullName = editFullName,
            phoneNumber = phoneNumber,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromDomain(customer: Customer): CustomerEntity {
            return CustomerEntity(
                id = customer.id,
                fullName = customer.fullName,
                editFullName = customer.editFullName,
                phoneNumber = customer.phoneNumber,
                createdAt = customer.createdAt
            )
        }
    }
}
