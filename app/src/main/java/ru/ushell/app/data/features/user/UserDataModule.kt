package ru.ushell.app.data.features.user

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import ru.ushell.app.base.UserDatabase
import ru.ushell.app.data.features.user.auth.AuthRepository
import ru.ushell.app.data.features.user.auth.retrofit.RetrofitAuthDataSource
import ru.ushell.app.data.features.user.remote.auth.AuthApi
import ru.ushell.app.data.features.user.room.RoomUserDataSource
import javax.inject.Singleton

@Module
//@InstallIn(ApplicationComponent)
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
    ): AuthRepository =
        AuthRepository(local, remote)

}