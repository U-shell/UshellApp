package ru.ushell.app.ui.utils.calendar.week

import ru.ushell.app.models.modelTimeTable.lesson.Lesson.LessonIsDate
import java.time.LocalDate
import ru.ushell.app.ui.utils.calendar.CalendarUtils.daysInWeekArray

class CalendarDataSource {

    val today: LocalDate
        get() { return LocalDate.now() }

    fun getDataWeek(selectedDate: LocalDate): CalendarData {
        val visibleDates = daysInWeekArray(selectedDate)
        return toUiModel(visibleDates, selectedDate)
    }

    private fun toUiModel(dateList: List<LocalDate>, lastSelectedDate: LocalDate): CalendarData {
        return CalendarData(
            selectedDate = toItemUiModel(
                date = lastSelectedDate,
                isSelectedDate = true,
                isLesson = LessonIsDate(lastSelectedDate)
            ),
            visibleDates = dateList.map {
                toItemUiModel(
                    date = it,
                    isSelectedDate = it.isEqual(lastSelectedDate),
                    isLesson = LessonIsDate(it)
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

