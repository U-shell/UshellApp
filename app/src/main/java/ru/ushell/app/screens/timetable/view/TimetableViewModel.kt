package ru.ushell.app.screens.timetable.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.attendance.AttendanceRepository
import ru.ushell.app.data.features.timetabel.TimetableRepository
import ru.ushell.app.screens.timetable.calendar.CalendarUtils.formattedDateDayAttendance

import java.time.LocalDate

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val timetableRepository: TimetableRepository,
    private val attendanceRepository: AttendanceRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<TimetableUiState>(TimetableUiState.Empty)
    val uiState: StateFlow<TimetableUiState> = _uiState.asStateFlow()

    fun loadTimetable(date: LocalDate) {
        viewModelScope.launch {
            try {
                _uiState.value = TimetableUiState.Loading

                _uiState.value = TimetableUiState.Success(
                    timetableRepository.getTimetable(date),
                    attendanceRepository.getAttendance(formattedDateDayAttendance(date))
                )

            } catch (e: Exception) {
                _uiState.value = TimetableUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
