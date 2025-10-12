package ru.ushell.app.screens

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.ushell.app.data.condition.session.Session
import ru.ushell.app.data.common.service.LoadDataService
import ru.ushell.app.navigation.ScreenNav
import ru.ushell.app.ui.theme.NoNavigationBarColorTheme
import ru.ushell.app.ui.theme.UshellAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loadDataService: LoadDataService by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            UshellAppTheme {
                MainApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadDataService.loadData()
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val isLoggedIn = Session.isLogin(LocalContext.current)

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.Main.route else Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
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
                FirstScreen( {
                    navController.navigate(Routes.Auth.route)
                })
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
