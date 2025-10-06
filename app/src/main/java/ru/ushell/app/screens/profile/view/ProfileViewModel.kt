package ru.ushell.app.screens.profile.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.user.UserRepository

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Empty)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun getNameUser(){
        viewModelScope.launch {
            try {
                _uiState.value = ProfileUiState.Loading
                val user = userRepository.getInfoUser()
                val name = "${user.firstName} ${user.lastName} ${user.patronymic}"
                val brief = user.title
                _uiState.value = ProfileUiState.Success(name,brief)
                } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    // TODO: добаить загушку

}