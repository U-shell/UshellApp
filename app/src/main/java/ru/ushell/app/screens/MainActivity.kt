package ru.ushell.app.screens

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import ru.ushell.app.domain.service.loadData.LoadDataService
import ru.ushell.app.navigation.MainNavHost
import ru.ushell.app.ui.theme.UshellAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var loadDataService: LoadDataService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            UshellAppTheme {
                MainApp(loadDataService = loadDataService)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadDataService.loadData()
    }
}

@Composable
fun MainApp(
    loadDataService: LoadDataService
) {
    val context = LocalContext.current
    var isLoggedIn by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        loadDataService.validSession(context) { result ->
            isLoggedIn = result
            isLoading = false
        }
    }

    if (isLoading) {
        SplashScreen{}
    } else {
        MainNavHost(isLoggedIn)
    }
}
