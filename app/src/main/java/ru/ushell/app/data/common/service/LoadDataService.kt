package ru.ushell.app.data.common.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.attendance.AttendanceRepository
import ru.ushell.app.data.features.timetabel.TimetableRepository
import ru.ushell.app.data.features.user.UserRepository

@HiltViewModel
class LoadDataService@Inject constructor(
    private val userRepository: UserRepository,
    private val timetableRepository: TimetableRepository,
    private val attendanceRepository: AttendanceRepository,
): ViewModel() {

    fun loadData(){
        viewModelScope.launch {
            try {
                println(userRepository.activeUser())
                if (!userRepository.activeUser()) return@launch

                timetableRepository.saveTimetable()
                attendanceRepository.saveAttendance()

            } catch (e: Exception) {
                println("Error loading timetable: $e")
            }
        }
    }
}