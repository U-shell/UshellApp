package ru.ushell.app.data.common.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.attendance.AttendanceRepository
import ru.ushell.app.data.features.messanger.MessengerRepository
import ru.ushell.app.data.features.messanger.remote.InfoUserMessengerResponse
import ru.ushell.app.data.features.timetabel.TimetableRepository
import ru.ushell.app.data.features.user.UserRepository

@HiltViewModel
class LoadDataService@Inject constructor(
    private val userRepository: UserRepository,
    private val timetableRepository: TimetableRepository,
    private val attendanceRepository: AttendanceRepository,
    private val messengerRepository: MessengerRepository
): ViewModel() {

    fun loadData(){
        viewModelScope.launch {
            try {
                println(userRepository.activeUser())
                if (!userRepository.activeUser()) return@launch

                timetableRepository.saveTimetable()
                attendanceRepository.saveAttendance()

                messengerRepository.getInfoUserMessenger()
                messengerRepository.getAllUser()

            } catch (e: Exception) {
                println("Error loading timetable: $e")
                e.message
            }
        }
    }
}