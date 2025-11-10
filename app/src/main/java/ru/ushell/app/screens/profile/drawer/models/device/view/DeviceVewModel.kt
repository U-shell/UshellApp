package ru.ushell.app.screens.profile.drawer.models.device.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.user.UserRepository

@HiltViewModel
class DeviceVewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun sendMessage(code: String) {
        viewModelScope.launch {
            try {
                userRepository.connectWebSocket()
                userRepository.sendMessage(code)
                userRepository.disconnectWebSocket()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendFile(code: String,resenderId: String, fileName: String){
        viewModelScope.launch {
            try {
                userRepository.connectWebSocket()
                userRepository.sendFile(
                    code = code,
                    resenderId = resenderId,
                    fileName = fileName
                )
                userRepository.disconnectWebSocket()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}