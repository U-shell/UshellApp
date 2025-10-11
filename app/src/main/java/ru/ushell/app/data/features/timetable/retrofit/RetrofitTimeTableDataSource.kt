package ru.ushell.app.data.features.timetable.retrofit

import ru.ushell.app.data.features.timetable.TimetableRemoteDataSource
import ru.ushell.app.data.features.timetable.remote.timetable.TimetableApi
import ru.ushell.app.data.features.timetable.remote.timetable.TimetableResponse

class RetrofitTimeTableDataSource(val timetableApi: TimetableApi) : TimetableRemoteDataSource {

    override suspend fun getTimetableGroup(groupId: Int): TimetableResponse = timetableApi.getTimetableGroup(groupId)

    override suspend fun getTimetableTeacher(): TimetableResponse = timetableApi.getTimetableTeacher()

}