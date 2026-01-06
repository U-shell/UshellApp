package ru.ushell.app.screens.schedule.calendar.week

import ru.ushell.app.data.features.timetable.mappers.lesson.lessonExistDate
import ru.ushell.app.screens.schedule.calendar.CalendarUtils
import java.time.LocalDate

class CalendarDateSource() {

    val today: LocalDate
        get() { return LocalDate.now() }

    fun getDataWeek(selectedDate: LocalDate): CalendarDate {
        val visibleDates =
            CalendarUtils.daysInWeekArray(
                selectedDate
            )
        return toUiModel(visibleDates, selectedDate)
    }

    private fun toUiModel(dateList: List<LocalDate>, lastSelectedDate: LocalDate): CalendarDate {

        return CalendarDate(
            selectedDate = toItemUiModel(
                date = lastSelectedDate,
                isSelectedDate = true,
                isLesson = lessonExistDate(lastSelectedDate)
            ),
            visibleDates = dateList.map {
                toItemUiModel(
                    date = it,
                    isSelectedDate = it.isEqual(lastSelectedDate),
                    isLesson = lessonExistDate(it)
                )
            },
        )
    }

    private fun toItemUiModel(date: LocalDate, isSelectedDate: Boolean, isLesson: Boolean) =
        CalendarDate.Date(
            isSelected = isSelectedDate,
            isLesson = isLesson,
            date = date,
        )
}

