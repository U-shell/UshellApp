package ru.ushell.app.screens.auth.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.user.UserRepository
import ru.ushell.app.domain.service.session.Session
import ru.ushell.app.screens.error.getErrorInternetMessage

@HiltViewModel
open class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Empty)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, context: Context) {

        if (email.isBlank() || password.isBlank() ) {
            _uiState.value = AuthUiState.Error("Введите логин и пароль")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {

                userRepository.loginUser(email, password)
                _uiState.value = AuthUiState.Success

                Session.setLogin(context)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(getErrorInternetMessage(e))
            }
        }
    }

    fun clearError() {
        if (_uiState.value is AuthUiState.Error) {
            _uiState.value = AuthUiState.Empty
        }
    }
}
