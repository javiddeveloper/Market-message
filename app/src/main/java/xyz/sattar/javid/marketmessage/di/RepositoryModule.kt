package xyz.sattar.javid.marketmessage.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.sattar.javid.marketmessage.data.repository.CustomerRepositoryImpl
import xyz.sattar.javid.marketmessage.data.repository.MessageRepositoryImpl
import xyz.sattar.javid.marketmessage.domain.repository.CustomerRepository
import xyz.sattar.javid.marketmessage.domain.repository.MessageRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(
        customerRepositoryImpl: CustomerRepositoryImpl
    ): CustomerRepository
}
