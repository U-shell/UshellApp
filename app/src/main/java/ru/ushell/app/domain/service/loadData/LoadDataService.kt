package ru.ushell.app.domain.service.loadData

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.attendance.AttendanceRepository
import ru.ushell.app.data.features.messenger.MessengerRepository
import ru.ushell.app.data.features.timetable.TimetableRepository
import ru.ushell.app.data.features.user.UserRepository
import ru.ushell.app.domain.service.session.Session
import ru.ushell.app.domain.service.token.TokenService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadDataService @Inject constructor(
    private val tokenService: TokenService,
    private val userRepository: UserRepository,
    private val timetableRepository: TimetableRepository,
    private val attendanceRepository: AttendanceRepository,
    private val messengerRepository: MessengerRepository
) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun validSession(context: Context, onResult: (Boolean) -> Unit) {
        scope.launch {
            try {
                val isLogin = if (Session.isLogin(context)) {
                    true
                } else {
                    val activeUser = userRepository.activeUser()
                    if (activeUser) Session.setLogin(context)
                    activeUser
                }
                onResult(isLogin)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun loadData() {
        scope.launch {
            try {
                if (!userRepository.activeUser()) return@launch

                if (!validToken()) return@launch

                // Запуск задач параллельно, чтобы не блокировать друг друга
                listOf(
                    scope.async {
                        runCatching { timetableRepository.saveTimetable() }
                            .onFailure { e -> println("Timetable load error: $e") }
                    },
                    scope.async {
                        runCatching { attendanceRepository.saveAttendance() }
                            .onFailure { e -> println("Attendance load error: $e") }
                    },
                    scope.async {
                        runCatching { messengerRepository.getInfoUserMessenger() }
                            .onFailure { e -> println("Messenger info error: $e") }
                    },
                    scope.async {
                        runCatching { messengerRepository.getAllUser() }
                            .onFailure { e -> println("Messenger users error: $e") }
                    }
                ).awaitAll()

                scheduleTokenRefresh(tokenService.getTimeToken())

            } catch (e: Exception) {
                println("Error in loadData: $e")
            }
        }
    }

    fun logout(context: Context) {
        scope.launch {
            try {
                Session.userLogout(context)
                userRepository.logoutUser()
            } catch (e: Exception) {
                println("Error during logout: $e")
            }
        }
    }

    private fun scheduleTokenRefresh(delayMs: Long) {
        scope.launch {
            delay(delayMs)
            if (!tokenService.isTokenValid()) {
                validToken()
            }
        }
    }

    private suspend fun validToken(): Boolean {
        return try {
            if (tokenService.isTokenValid() && tokenService.getAccessToken() != null) {
                true
            } else {
                updateToken()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateToken(): Boolean {
        return try {
            val token = userRepository.refreshAccessToken()
            tokenService.saveAccessToken(token.accessToken, token.validationAccess)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}