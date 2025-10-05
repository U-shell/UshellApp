package ru.ushell.app.screens.auth.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.user.UserRepository

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: UserRepository
): ViewModel() {


    fun loginUser(email: String, password: String): Boolean {
        viewModelScope.launch {
            try {
//                val response = authRepository.loginUser(email, password)
//                print(response)
                println("User loaded: ${authRepository.getInfoUser()}")
                // обработка ответа
            } catch (e: Exception) {
                println(e)
                // обработка ошибки
            }
        }
        return false
    }
}