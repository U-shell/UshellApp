package ru.ushell.app.data.features.attendance

import ru.ushell.app.data.features.attendance.remote.attendance.AttendanceGroupDayResponse
import ru.ushell.app.data.features.attendance.remote.attendance.AttendanceResponse
import ru.ushell.app.data.features.attendance.dto.AttendanceGroupRequest

interface AttendanceRemoteDataSource {

    suspend fun getAttendanceStudent(): AttendanceResponse

    suspend fun getAttendanceGroupDay(
        groupId: Int,
        date: String,
        numLesson: Int
    ): AttendanceGroupDayResponse

    suspend fun putAttendanceGroup(groupId: Int, attendance: AttendanceGroupRequest)

}