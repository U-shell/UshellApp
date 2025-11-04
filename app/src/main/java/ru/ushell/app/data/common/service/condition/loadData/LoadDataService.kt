package ru.ushell.app.data.common.service.condition.loadData

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ushell.app.data.common.service.TokenService
import ru.ushell.app.data.common.service.condition.session.Session
import ru.ushell.app.data.features.attendance.AttendanceRepository
import ru.ushell.app.data.features.messenger.MessengerRepository
import ru.ushell.app.data.features.timetable.TimetableRepository
import ru.ushell.app.data.features.user.UserRepository

@HiltViewModel
class LoadDataService @Inject constructor(
    private val tokenService: TokenService,
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

                if (!validToken()) return@launch
//TODO: игнорировать сервисы которые недоступны чтобы те не блокировали загрузку остальных
//                timetableRepository.saveTimetable()
//                attendanceRepository.saveAttendance()

                messengerRepository.getInfoUserMessenger()
                messengerRepository.getAllUser()

                scheduleTokenRefresh(tokenService.getTimeToken())

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

    private fun scheduleTokenRefresh(delay: Long) {

        viewModelScope.launch {
            delay(delay)
            if (tokenService.isTokenValid().not()) {
                validToken()
            }
        }
    }

    private suspend fun validToken(): Boolean {
        try {
            return if (tokenService.isTokenValid() &&  tokenService.getAccessToken() != null) {
                true
            } else {
                updateToken()
            }

        } catch (e: Exception) {
            println("Error loading timetable: $e")
            e.message
            return false
        }
    }

    private suspend fun updateToken(): Boolean {
        val token = userRepository.refreshAccessToken()
        tokenService.saveAccessToken(token.accessToken,token.validationAccess)
        return true
    }
}