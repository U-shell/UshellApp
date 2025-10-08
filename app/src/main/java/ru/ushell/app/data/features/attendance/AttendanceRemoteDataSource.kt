package ru.ushell.app.data.features.attendance

import ru.ushell.app.data.features.attendance.remote.attendance.AttendanceResponse

interface AttendanceRemoteDataSource {

    suspend fun getAttendanceStudent(): AttendanceResponse
}