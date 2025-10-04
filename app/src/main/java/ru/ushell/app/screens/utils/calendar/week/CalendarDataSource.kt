package ru.ushell.app.screens.utils.calendar.week

import ru.ushell.app.screens.utils.calendar.CalendarUtils
import java.time.LocalDate

class CalendarDataSource {

    val today: LocalDate
        get() { return LocalDate.now() }

    fun getDataWeek(selectedDate: LocalDate): CalendarData {
        val visibleDates =
            CalendarUtils.daysInWeekArray(
                selectedDate
            )
        return toUiModel(visibleDates, selectedDate)
    }

    private fun toUiModel(dateList: List<LocalDate>, lastSelectedDate: LocalDate): CalendarData {
        return CalendarData(
            selectedDate = toItemUiModel(
                date = lastSelectedDate,
                isSelectedDate = true,
                isLesson = false
//                isLesson = Lesson.LessonIsDate(
//                    lastSelectedDate
//                )
            ),
            visibleDates = dateList.map {
                toItemUiModel(
                    date = it,
                    isSelectedDate = it.isEqual(lastSelectedDate),
                    isLesson = false
//                    isLesson = Lesson.LessonIsDate(
//                        it
//                    )
                )
            },
        )
    }

    private fun toItemUiModel(date: LocalDate, isSelectedDate: Boolean, isLesson: Boolean) =
        CalendarData.Date(
            isSelected = isSelectedDate,
            isLesson = isLesson,
            date = date,
        )
}

