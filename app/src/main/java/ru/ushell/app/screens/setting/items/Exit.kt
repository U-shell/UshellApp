package ru.ushell.app.screens.setting.items

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.alpha
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.user.UserRepository
import ru.ushell.app.domain.service.session.Session
import ru.ushell.app.navigation.Destination


@Composable
fun ExitButton(
    navController: NavHostController
){
    val exit = remember { mutableStateOf(false) }
    Box(
    ){
        TextButton(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Red.copy(alpha = 0.2f))
                .fillMaxWidth(),
            onClick = {
                exit.value = true
                navController.navigate(Destination.Auth.route){
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        ) {
            Text(
                text = "Выход",
                color = Color.White
            )
        }
    }

    if(exit.value){
        Exit()
        exit.value=false
    }

}

@Composable
fun Exit (
    logoutViewModel: LogoutViewModel = hiltViewModel()
){
    logoutViewModel.logout(LocalContext.current)
}

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LogoutUiState>(LogoutUiState.Empty)
    val uiState: StateFlow<LogoutUiState> = _uiState.asStateFlow()

    fun logout(context: Context){
        viewModelScope.launch {
            _uiState.value = LogoutUiState.Loading

            try {

                Session.userLogout(context)
                userRepository.logoutUser()

                _uiState.value = LogoutUiState.Success

            } catch (e: Exception) {
                _uiState.value = LogoutUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed interface LogoutUiState {
    object Empty : LogoutUiState
    object Loading : LogoutUiState
    object Success: LogoutUiState
    data class Error(val message: String) : LogoutUiState
}



@Preview
@Composable
fun ExitButtonPreview(){
    ExitButton(
        rememberNavController()
    )
}