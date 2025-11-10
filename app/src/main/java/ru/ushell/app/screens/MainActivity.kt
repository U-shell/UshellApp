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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.ushell.app.domain.service.loadData.LoadDataService
import ru.ushell.app.navigation.ScreenNav
import ru.ushell.app.ui.theme.NoNavigationBarColorTheme
import ru.ushell.app.ui.theme.UshellAppTheme
import javax.inject.Inject

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
        Navigation(isLoggedIn)
    }
}

@Composable
fun Navigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.Main.route else Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            println("SplashScreen")
            SplashScreen {
                navController.navigate(
                    if (isLoggedIn) Routes.Main.route else Routes.Start.route
                ) {
                    popUpTo(Routes.Splash.route) { inclusive = true }
                }
            }
        }

        composable(Routes.Start.route) {
            NoNavigationBarColorTheme {
                FirstScreen {
                    navController.navigate(Routes.Auth.route)
                }
            }
        }

        composable(Routes.Auth.route) {
            NoNavigationBarColorTheme {
                AuthorizeScreen(navController)
            }
        }

        composable(Routes.Main.route) {
            UshellAppTheme {
                ScreenNav()
            }
        }
    }
}

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Start : Routes("start_start")
    data object Auth : Routes("start_auth")
    data object Main : Routes("nav_screen")
}
