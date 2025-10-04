package ru.ushell.app.screens.utils.calendar.month

import android.annotation.SuppressLint
import ru.ushell.app.screens.utils.calendar.CalendarUtils
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@SuppressLint("NewApi")
internal fun YearMonth.getWeeks(
  includeAdjacentMonths: Boolean,
  firstDayOfTheWeek: DayOfWeek,
  today: LocalDate,
): List<WeekDays> {
  val daysLength = lengthOfMonth()

  val starOffset = (atDay(1).dayOfWeek daysUntil firstDayOfTheWeek)
  val endOffset = CalendarUtils.DaysInAWeek - (atDay(daysLength).dayOfWeek daysUntil firstDayOfTheWeek)
  val allDays = (1 - starOffset..daysLength + endOffset).toMutableList()

  for (i in allDays.size - 1 downTo 0 step 7) {
    allDays.removeAt(i)
  }

  return allDays.chunked(CalendarUtils.DaysInAWeek).mapIndexed { index, days ->
    WeekDays(
      isFirstWeekOfTheMonth = index == 0,
      days = days.mapNotNull { dayOfMonth ->
        val (date, isFromCurrentMonth) = when (dayOfMonth) {

          in Int.MIN_VALUE..0 -> if (includeAdjacentMonths) {
            val previousMonth = this.minusMonths(1)
            previousMonth.atDay(previousMonth.lengthOfMonth() + dayOfMonth) to false
          } else {
            return@mapNotNull null
          }

          in 1..daysLength -> atDay(dayOfMonth) to true
          else -> if (includeAdjacentMonths) {
            val previousMonth = this.plusMonths(1)
            previousMonth.atDay(dayOfMonth - daysLength) to false
          } else {
            return@mapNotNull null
          }
        }

        WeekDay(
          date = date,
          isFromCurrentMonth = isFromCurrentMonth,
          isCurrentDay = date.equals(today),
        )
      }
    )
  }
}
