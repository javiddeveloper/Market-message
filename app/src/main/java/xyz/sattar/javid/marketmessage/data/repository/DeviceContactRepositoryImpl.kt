package xyz.sattar.javid.marketmessage.data.repository

import android.content.Context
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import xyz.sattar.javid.marketmessage.domain.model.DeviceContact
import xyz.sattar.javid.marketmessage.domain.repository.DeviceContactRepository
import javax.inject.Inject

class DeviceContactRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceContactRepository {

    override fun getDeviceContacts(): Flow<List<DeviceContact>> = flow {
        val contacts = mutableListOf<DeviceContact>()
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                if (idIndex != -1 && nameIndex != -1 && numberIndex != -1) {
                    val id = it.getString(idIndex)
                    val name = it.getString(nameIndex) ?: "Unknown"
                    val number = it.getString(numberIndex)
                    contacts.add(DeviceContact(id, name, number))
                }
            }
        }
        emit(contacts.distinctBy { it.phoneNumber }) // Remove duplicates
    }.flowOn(Dispatchers.IO)
}
