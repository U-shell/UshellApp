package ru.ushell.app.data.features.attendance.room

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.ushell.app.data.features.attendance.AttendanceLocalDataSource
import ru.ushell.app.data.features.attendance.remote.attendance.AttendanceResponse
import ru.ushell.app.data.features.attendance.dto.Attendance
import ru.ushell.app.data.features.attendance.room.dao.AttendanceDao
import ru.ushell.app.data.features.attendance.room.dao.AttendanceEntity

class RoomAttendanceDataSource(
    private val gson: Gson,
    private val attendanceDao: AttendanceDao
): AttendanceLocalDataSource {


    override suspend fun saveAttendance(username: String, attendanceResponse: AttendanceResponse) =
        attendanceDao.saveAttendance(
            AttendanceEntity(
                username = username,
                statistic = attendanceResponse.statistic,
                attendance = gson.toJson(attendanceResponse.attendance)
            )
        )

    override suspend fun getAttendance(username: String, date: String): List<Attendance>{
        val entity = attendanceDao.getAttendance(username)

        val attendanceMap: Map<String, Map<String, String>> = try {
            gson.fromJson(entity.attendance, object : TypeToken<Map<String, Map<String, String>>>() {}.type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }

        return attendanceMap[date]?.mapNotNull { (numStr, statusStr) ->
            numStr.toIntOrNull()?.let { num ->
                Attendance(
                    date = date,
                    numLesson = num,
                    status = statusStr == "EXISTS"
                )
            }
        } ?: emptyList()
    }


    override suspend fun getStatistic(username: String): Int = attendanceDao.getStatistic(username)

}