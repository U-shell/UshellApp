package ru.ushell.app.data.common.utils

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DateTimeUtil {
    companion object{

        fun parseDate(dateLesson: String): String {
            return LocalDate.parse(dateLesson, DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()
        }

        fun parseTime(timeLesson: String): String {
            return LocalTime.parse(timeLesson, DateTimeFormatter.ofPattern("HH:mm:ss")).toString()
        }
    }
}