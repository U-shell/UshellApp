package ru.ushell.app.api.template


data class TimetableResponse(

    val mainSchedule: Map<String, Map<String, Map<String, Lesson>>> = emptyMap(),

    val secondarySchedule: Map<String, Map<String, Map<String, Lesson>>> = emptyMap()
)

data class Lesson(
    val timeStart: String,
    val timeEnd: String,
    val subgroup: Int,
    val classroom: String,
    val withWhom: String,
    val subjectName: String,
    val fullType: String
)
