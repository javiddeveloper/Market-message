package xyz.sattar.javid.marketmessage.domain.repository

import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.DeviceContact

interface DeviceContactRepository {
    fun getDeviceContacts(): Flow<List<DeviceContact>>
}
