package ru.ushell.app.data.features.timetabel

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ushell.app.base.UserDatabase
import ru.ushell.app.data.features.timetabel.remote.timetable.TimetableApi
import ru.ushell.app.data.features.timetabel.retrofit.RetrofitTimeTableDataSource
import ru.ushell.app.data.features.timetabel.room.RoomTimetableDataSource
import ru.ushell.app.data.features.user.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TimetableDataModule {

    @Provides
    @Singleton
    fun providesLocalDataSource(roomDatabase: UserDatabase): TimetableLocalDataSource =
        RoomTimetableDataSource(
            roomDatabase.timetableDao(),
            roomDatabase.timetableSecondaryDao()
        )

    @Provides
    @Singleton
    fun providesTimetableRemoteDataSource(timetableApi: TimetableApi): TimetableRemoteDataSource =
        RetrofitTimeTableDataSource(timetableApi)

    @Provides
    @Singleton
    fun providesTimetableRepository(
        local: TimetableLocalDataSource,
        remote: TimetableRemoteDataSource,
        repository: UserRepository
    ): TimetableRepository =
        TimetableRepository(
            local,
            remote,
            repository
        )
}