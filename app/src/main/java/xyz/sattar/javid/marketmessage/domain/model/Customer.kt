package xyz.sattar.javid.marketmessage.domain.model

data class Customer(
    val id: Long = 0,
    val fullName: String,
    val editFullName: String,
    val phoneNumber: String,
    val createdAt: Long
)
