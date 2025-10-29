package ru.ushell.app.data.common.service.condition.loadData

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ushell.app.data.common.service.condition.session.Session
import ru.ushell.app.data.features.attendance.AttendanceRepository
import ru.ushell.app.data.features.messenger.MessengerRepository
import ru.ushell.app.data.features.timetable.TimetableRepository
import ru.ushell.app.data.features.user.UserRepository

@HiltViewModel
class LoadDataService @Inject constructor(
    private val userRepository: UserRepository,
    private val timetableRepository: TimetableRepository,
    private val attendanceRepository: AttendanceRepository,
    private val messengerRepository: MessengerRepository
): ViewModel() {

    private val _state = MutableStateFlow<LoadDataState>(LoadDataState.Empty)
    val state : StateFlow<LoadDataState> = _state.asStateFlow()


    fun validSession(context: Context){
        viewModelScope.launch {
            _state.value = LoadDataState.Loading
            try {

                if (Session.isLogin(context)){
                    _state.value = LoadDataState.Success(true)
                    return@launch
                }

                val isLogin = userRepository.activeUser()

                _state.value = LoadDataState.Success(isLogin)

                if (isLogin) Session.setLogin(context)

            } catch (e: Exception ){
                _state.value = LoadDataState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadData(){
        viewModelScope.launch {
            try {
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

    fun logout(context: Context){
        viewModelScope.launch {
            try {
                Session.userLogout(context)
                userRepository.logoutUser()
            } catch (e: Exception) {
                println("Error loading timetable: $e")
                e.message
            }
        }
    }

}