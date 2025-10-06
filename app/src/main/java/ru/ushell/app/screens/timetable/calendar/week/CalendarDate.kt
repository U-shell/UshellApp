package ru.ushell.app.screens.timetable.calendar.week

import java.time.LocalDate

data class CalendarDate(
    var selectedDate: Date,
    val visibleDates: List<Date>
){
    data class Date(
        var date: LocalDate,
        val isSelected: Boolean,
        val isLesson: Boolean
    ){
        fun minusWeeks(): LocalDate = copy(date = date.minusWeeks(1)).date
        fun plusWeeks(): LocalDate = copy(date = date.plusWeeks(1)).date
        fun plusWeeks(plus: Long): LocalDate = copy(date = date.plusWeeks(plus)).date
    }
}
