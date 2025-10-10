package ru.ushell.app.data.features.messanger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ushell.app.base.UserDatabase
import ru.ushell.app.data.features.messanger.remote.MessengerApi
import ru.ushell.app.data.features.messanger.retrofir.RetrofitMessengerDataSource
import ru.ushell.app.data.features.messanger.room.RoomMessengerDataSource
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
