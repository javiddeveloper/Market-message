package xyz.sattar.javid.marketmessage.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import xyz.sattar.javid.marketmessage.data.local.AppDatabase
import xyz.sattar.javid.marketmessage.data.local.dao.CustomerDao
import xyz.sattar.javid.marketmessage.data.local.dao.MessageDao
import xyz.sattar.javid.marketmessage.data.local.dao.ReadyMessageDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "market_message_db"
        )
            .fallbackToDestructiveMigration() // For development simplicity
            .build()
    }

    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    fun provideCustomerDao(database: AppDatabase): CustomerDao {
        return database.customerDao()
    }

    @Provides
    fun provideReadyMessageDao(database: AppDatabase): ReadyMessageDao {
        return database.readyMessageDao()
    }
}
