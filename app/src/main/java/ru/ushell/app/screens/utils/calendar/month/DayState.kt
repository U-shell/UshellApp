package ru.ushell.app.screens.utils.calendar.month

import androidx.compose.runtime.Stable

/**
 * Contains information about current selection as well as date of rendered day
 */
@Stable
data class DayState<T : SelectionState>(
    private val day: Day,
    val selectionState: T,
) : Day by day
