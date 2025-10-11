package ru.ushell.app.data.features.messenger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ushell.app.base.UserDatabase
import ru.ushell.app.data.features.messenger.remote.MessengerApi
import ru.ushell.app.data.features.messenger.retrofir.RetrofitMessengerDataSource
import ru.ushell.app.data.features.messenger.room.RoomMessengerDataSource
import ru.ushell.app.data.features.user.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class MessengerDataModule {
    @Provides
    @Singleton
    fun providesLocalDataSource(roomDatabase: UserDatabase): MessengerLocalDataSource =
        RoomMessengerDataSource(roomDatabase.messengerDao())

    @Provides
    @Singleton
    fun providesMessengerRemoteDataSource(messengerApi: MessengerApi): MessengerRemoteDataSource=
        RetrofitMessengerDataSource(messengerApi)

    @Provides
    @Singleton
    fun providesMessengerRepository(
        local: MessengerLocalDataSource,
        remote: MessengerRemoteDataSource,
        repository: UserRepository
    ): MessengerRepository =
        MessengerRepository(
            local,
            remote,
            repository)

}
