package ru.ushell.app.data.features.user

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ushell.app.base.UserDatabase
import ru.ushell.app.data.features.user.remote.auth.AuthApi
import ru.ushell.app.data.features.user.retrofit.RetrofitAuthDataSource
import ru.ushell.app.data.features.user.room.RoomUserDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserDataModule {

    @Provides
    @Singleton
    fun providesLocalDataSource(roomDatabase: UserDatabase): UserLocalDataSource =
        RoomUserDataSource(roomDatabase.userDao())


    @Provides
    @Singleton
    fun providesAuthRemoteDataSource(authApi: AuthApi): UserRemoteDataSource =
        RetrofitAuthDataSource(authApi)

    @Provides
    @Singleton
    fun providesAuthRepository(
        local: UserLocalDataSource,
        remote: UserRemoteDataSource
    ): UserRepository =
        UserRepository(local, remote)

}