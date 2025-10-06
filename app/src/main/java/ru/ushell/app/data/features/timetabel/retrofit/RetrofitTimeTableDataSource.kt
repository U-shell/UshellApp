package ru.ushell.app.data.features.timetabel.retrofit

import ru.ushell.app.data.features.timetabel.TimetableRemoteDataSource
import ru.ushell.app.data.features.timetabel.remote.timetable.TimetableApi
import ru.ushell.app.data.features.timetabel.remote.timetable.TimetableResponse

class RetrofitTimeTableDataSource(val timetableApi: TimetableApi) : TimetableRemoteDataSource {

    override suspend fun getTimetableGroup(groupId: Int): TimetableResponse = timetableApi.getTimetableGroup(groupId)

    override suspend fun getTimetableTeacher(): TimetableResponse = timetableApi.getTimetableTeacher()

}