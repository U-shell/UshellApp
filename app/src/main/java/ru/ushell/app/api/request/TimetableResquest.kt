package ru.ushell.app.api.request

import java.time.LocalDate
import java.time.LocalTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ushell.app.SQLite.DatabaseHelper
import ru.ushell.app.api.API.api
import ru.ushell.app.api.template.TimetableResponse
import java.net.HttpURLConnection
import java.time.format.DateTimeFormatter

interface TimetableCallback {
    fun onInfoUserReceived(timetable: Boolean)
}

fun timetableGroup(
    groupId: Int,
    databaseHelper: DatabaseHelper,
    callback: TimetableCallback?
){
    timetable(response = api.timetableGroup(groupId),
        databaseHelper,
        callback)
}

fun timetableTeacher(
    token: String,
    databaseHelper: DatabaseHelper,
    callback: TimetableCallback?
){
    timetable(response = api.timetableTeacher("Bearer $token"),
        databaseHelper,
        callback)
}

private fun timetable(
    response: Call<TimetableResponse>,
    databaseHelper: DatabaseHelper,
    callback: TimetableCallback?
){

    response.enqueue(object : Callback<TimetableResponse>{
        override fun onResponse(
            call: Call<TimetableResponse>,
            response: Response<TimetableResponse>
        ) {
            when{
                response.code() == HttpURLConnection.HTTP_OK && response.body() != null ->{
                    val body: TimetableResponse = response.body()!!
                    processing(body, databaseHelper)
                    callback?.onInfoUserReceived(true)
                }
                else -> {
                    callback?.onInfoUserReceived(false)
                }
            }
        }

        override fun onFailure(
            call: Call<TimetableResponse>,
            t: Throwable
        ) {
            callback?.onInfoUserReceived(false)
        }

    })
}

fun processing(
    response: TimetableResponse,
    databaseHelper: DatabaseHelper
){
    response.mainSchedule.forEach { (weekStr, days) ->
        val week = weekStr.toIntOrNull() ?: return@forEach
        days.forEach { (dayOfWeek, lessons) ->
            lessons.forEach { (lessonNumStr, lesson) ->
                val numLesson = lessonNumStr.toIntOrNull() ?: return@forEach
                databaseHelper.addMainSchedule(
                    week,
                    dayOfWeek,
                    numLesson,
                    parseTime(lesson.timeStart),
                    parseTime(lesson.timeEnd),
                    lesson.fullType,
                    lesson.subjectName,
                    lesson.subgroup,
                    lesson.withWhom.ifEmpty { " " },
                    -1, // или из данных, если есть
                    lesson.classroom
                )
            }
        }
    }

    response.secondarySchedule.forEach { (dateStr, days) ->
        val date = parseDate(dateStr)
        days.forEach { (dayOfWeek, lessons) ->
            lessons.forEach { (lessonNumStr, lesson) ->
                val numLesson = lessonNumStr.toIntOrNull() ?: return@forEach
                databaseHelper.addSecondarySchedule(
                    date,
                    dayOfWeek,
                    numLesson,
                    parseTime(lesson.timeStart),
                    parseTime(lesson.timeEnd),
                    lesson.fullType,
                    lesson.subjectName,
                    lesson.subgroup,
                    lesson.withWhom.ifEmpty { " " },
                    -1,
                    lesson.classroom
                )
            }
        }
    }
}

private fun parseTime(timeLesson: String): LocalTime {
    return LocalTime.parse(timeLesson, DateTimeFormatter.ofPattern("HH:mm:ss"))
}



private fun parseDate(dateLesson: String): LocalDate {
    return LocalDate.parse(dateLesson, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}
