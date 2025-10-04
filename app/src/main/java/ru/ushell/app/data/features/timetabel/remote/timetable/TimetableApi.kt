package ru.ushell.app.data.features.timetabel.remote.timetable

import retrofit2.http.GET

interface TimetableApi {

    @GET("./timetable/groups/group")
    fun getTimetableGroup(): TimetableResponse

    @GET("./timetable/teacher/me")
    fun getTimetableTeacher(): TimetableResponse

}