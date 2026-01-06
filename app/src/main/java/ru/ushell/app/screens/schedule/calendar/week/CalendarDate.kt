package ru.ushell.app.screens.schedule.calendar.week

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
        fun weeks(plus: Long): LocalDate = copy(date = date.plusWeeks(plus)).date
    }
}
