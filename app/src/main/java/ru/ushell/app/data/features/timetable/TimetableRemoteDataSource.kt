package ru.ushell.app.data.features.timetable

import ru.ushell.app.data.features.timetable.remote.timetable.TimetableResponse

interface TimetableRemoteDataSource {

    suspend fun getTimetableGroup(groupId: Int): TimetableResponse

    suspend fun getTimetableTeacher(): TimetableResponse
}