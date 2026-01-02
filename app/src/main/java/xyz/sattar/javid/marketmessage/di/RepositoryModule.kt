package xyz.sattar.javid.marketmessage.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.sattar.javid.marketmessage.data.repository.CustomerRepositoryImpl
import xyz.sattar.javid.marketmessage.data.repository.DeviceContactRepositoryImpl
import xyz.sattar.javid.marketmessage.data.repository.MessageDraftRepositoryImpl
import xyz.sattar.javid.marketmessage.data.repository.MessageRepositoryImpl
import xyz.sattar.javid.marketmessage.data.repository.ReadyMessageRepositoryImpl
import xyz.sattar.javid.marketmessage.data.repository.ThemeRepositoryImpl
import xyz.sattar.javid.marketmessage.domain.repository.CustomerRepository
import xyz.sattar.javid.marketmessage.domain.repository.DeviceContactRepository
import xyz.sattar.javid.marketmessage.domain.repository.MessageDraftRepository
import xyz.sattar.javid.marketmessage.domain.repository.MessageRepository
import xyz.sattar.javid.marketmessage.domain.repository.ReadyMessageRepository
import xyz.sattar.javid.marketmessage.domain.repository.ThemeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindThemeRepository(
        themeRepositoryImpl: ThemeRepositoryImpl
    ): ThemeRepository

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(
        customerRepositoryImpl: CustomerRepositoryImpl
    ): CustomerRepository

    @Binds
    @Singleton
    abstract fun bindDeviceContactRepository(
        deviceContactRepositoryImpl: DeviceContactRepositoryImpl
    ): DeviceContactRepository

    @Binds
    @Singleton
    abstract fun bindMessageDraftRepository(
        messageDraftRepositoryImpl: MessageDraftRepositoryImpl
    ): MessageDraftRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Binds
    @Singleton
    abstract fun bindReadyMessageRepository(
        readyMessageRepositoryImpl: ReadyMessageRepositoryImpl
    ): ReadyMessageRepository
}
