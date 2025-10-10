package ru.ushell.app.data.features.attendance

import ru.ushell.app.data.features.attendance.dto.Attendance
import ru.ushell.app.data.features.attendance.dto.AttendanceGroup
import ru.ushell.app.data.features.attendance.dto.AttendanceGroupRequest
import ru.ushell.app.data.features.attendance.dto.AttendanceStudentGroup
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

    suspend fun getStatisticAttendance(): Int =
        attendanceLocalDataSource.getStatistic(userRepository.getUsername())

    suspend fun getAttendanceGroupDay(date: String, numLesson: Int, subgroup: Int): List<AttendanceGroup> =
        attendanceRemoteDataSource.getAttendanceGroupDay(
            userRepository.getGroupId(),
            date,
            numLesson
        ).attendance
            .filter { (_, student) -> student.subgroup == subgroup  || subgroup == 0 }
            .map { (studentId, student) ->
                AttendanceGroup(
                    id = studentId,
                    nameStudent = student.nameStudent,
                    subgroup = student.subgroup,
                    attendance = student.attendance
                )
        }

    suspend fun putAttendanceGroup(student: AttendanceStudentGroup) {

        attendanceRemoteDataSource.putAttendanceGroup(
            userRepository.getGroupId(),
            attendance = AttendanceGroupRequest(
                date = student.date,
                lesson = student.numLesson,
                list = mapOf(student.id to student.attendance)
            )
        )

    }
}