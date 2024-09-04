package ru.ushell.app.ui.utils.calendar.month

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
internal class WeekDay(
    override val date: LocalDate,
    override val isCurrentDay: Boolean,
    override val isFromCurrentMonth: Boolean
) : Day

@Immutable
internal data class WeekDays(
    val isFirstWeekOfTheMonth: Boolean = false,
    val days: List<Day>,
)
