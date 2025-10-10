package ru.ushell.app.data.features.attendance

import ru.ushell.app.data.features.attendance.remote.attendance.AttendanceResponse
import ru.ushell.app.data.features.attendance.dto.Attendance

interface AttendanceLocalDataSource {

    suspend fun saveAttendance(username: String, attendanceResponse: AttendanceResponse)

    suspend fun getAttendance(username: String, date: String): List<Attendance>

    suspend fun getStatistic(username: String): Int

}