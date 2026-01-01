package xyz.sattar.javid.marketmessage.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.sattar.javid.marketmessage.data.local.dao.CustomerDao
import xyz.sattar.javid.marketmessage.data.local.dao.MessageDao
import xyz.sattar.javid.marketmessage.data.local.entity.CustomerEntity
import xyz.sattar.javid.marketmessage.data.local.entity.MessageEntity

@Database(entities = [MessageEntity::class, CustomerEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun customerDao(): CustomerDao
}
