package ru.ushell.app.data.features.attendance

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ushell.app.base.UserDatabase
import ru.ushell.app.data.features.attendance.remote.attendance.AttendanceApi
import ru.ushell.app.data.features.attendance.retrofit.RetrofitAttendanceDataSource
import ru.ushell.app.data.features.attendance.room.RoomAttendanceDataSource
import ru.ushell.app.data.features.user.UserRepository

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AttendanceDataModule {

    @Provides
    @Singleton
    fun providesLocalDataSource(roomDatabase: UserDatabase): AttendanceLocalDataSource =
        RoomAttendanceDataSource(Gson(), roomDatabase.attendanceDao())


    @Provides
    @Singleton
    fun providesAttendanceRemoteDataSource(attendanceApi: AttendanceApi): AttendanceRemoteDataSource =
        RetrofitAttendanceDataSource(attendanceApi)

    @Provides
    @Singleton
    fun providesAttendanceRepository(
        local: AttendanceLocalDataSource,
        remote: AttendanceRemoteDataSource,
        repository: UserRepository
    ): AttendanceRepository =
        AttendanceRepository(
            local,
            remote,
            repository
        )
}
