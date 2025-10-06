package ru.ushell.app.data.features.timetabel

import ru.ushell.app.data.features.timetabel.remote.timetable.TimetableResponse

interface TimetableRemoteDataSource {

    suspend fun getTimetableGroup(groupId: Int): TimetableResponse

    suspend fun getTimetableTeacher(): TimetableResponse
}