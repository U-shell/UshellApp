package ru.ushell.app.data.features.attendance

import ru.ushell.app.data.features.attendance.remote.attendance.Status
import ru.ushell.app.data.features.attendance.room.dao.Attendance
import ru.ushell.app.data.features.user.UserRepository

class AttendanceRepository(
    private val attendanceLocalDataSource: AttendanceLocalDataSource,
    private val attendanceRemoteDataSource: AttendanceRemoteDataSource,
    private val userRepository: UserRepository,
) {

    suspend fun saveAttendance(){

        attendanceLocalDataSource.saveAttendance(
            userRepository.getUsername(),
            attendanceRemoteDataSource.getAttendanceStudent()
        )
    }

    suspend fun getAttendance(date: String): List<Attendance> =
        attendanceLocalDataSource.getAttendance(userRepository.getUsername(), date)

    suspend fun getStatisticAttendance(): Int = attendanceLocalDataSource.getStatistic(userRepository.getUsername())

    private fun getStatus(status: Status): Boolean{

        return when(status){
            Status.EXISTS -> true
            Status.NOT_EXISTS -> false
        }

    }
}