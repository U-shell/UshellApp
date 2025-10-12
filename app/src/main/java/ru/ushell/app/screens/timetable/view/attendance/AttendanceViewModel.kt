package ru.ushell.app.screens.timetable.view.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.attendance.AttendanceRepository
import ru.ushell.app.data.features.attendance.mappers.AttendanceStudentGroup

@HiltViewModel
class AttendanceViewModel  @Inject constructor(
    private val attendanceRepository: AttendanceRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<AttendanceUiState>(AttendanceUiState.Empty)
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    fun loadAttendanceGroup(date: String, numLesson: Int, subgroup: Int) {

        viewModelScope.launch {
            _uiState.value = AttendanceUiState.Loading

            try {

                _uiState.value = AttendanceUiState.Success(
                    attendanceRepository.getAttendanceGroupDay(date, numLesson,subgroup)
                )

            } catch (e: Exception) {
                _uiState.value = AttendanceUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun putAttendanceGroup(student: AttendanceStudentGroup) {
        viewModelScope.launch {

            try {
                attendanceRepository.putAttendanceGroup(student)
            } catch (e: Exception) {

            }
        }
    }
}