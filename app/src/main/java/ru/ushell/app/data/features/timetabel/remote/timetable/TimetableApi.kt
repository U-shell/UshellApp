package ru.ushell.app.data.features.timetabel.remote.timetable

import retrofit2.http.GET
import retrofit2.http.Query;

interface TimetableApi {

    @GET("./timetable/groups/group")
    suspend fun getTimetableGroup(@Query("id") groupId: Int): TimetableResponse

    @GET("./timetable/teacher/me")
    suspend fun getTimetableTeacher(): TimetableResponse

}