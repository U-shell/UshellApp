package ru.ushell.app

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.api.Service
import ru.ushell.app.models.User
import ru.ushell.app.models.e_class.ERoleClass.containsValueGroup
import ru.ushell.app.ui.navigation.ScreenNav
import ru.ushell.app.ui.screens.startScreen.StartScreen
import ru.ushell.app.ui.theme.UshellAppTheme
import ru.ushell.app.utils.SavingSession.SaveUser


@SuppressLint("CustomSplashScreen")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            User.getInstance(LocalContext.current)
            val navController = rememberNavController()
            val isLoggedIn = remember { mutableStateOf(SaveUser.isLogin(applicationContext)) }

            UshellAppTheme {
                SplashScreen(
                    onTimeout = {
                        setContent {
                            MainNavScreen(
                                isLoggedIn=isLoggedIn.value,
                                navController=navController
                            )
                        }
                    }
                )
            }
        }
    }


    override fun onStart() {
        super.onStart()
        User.getInstance(this)
        if (containsValueGroup()) {
            Service(this).updateData()
        }
//        TODO("подумать какие функции необходимо вызывать при первом заходе пользователя")
    }

    override fun onDestroy() {
        super.onDestroy()
//        TODO("какие функции надо вызвать при выходе их приложения")
    }
}

@Composable
private fun MainNavScreen(
    isLoggedIn: Boolean,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.ScreenNav.route else Routes.ScreenAuth.route) {
        composable(Routes.ScreenAuth.route) {
            StartScreen()
        }
        composable(Routes.ScreenNav.route) {
            ScreenNav()
        }
    }
}


sealed class Routes(
    val route: String,
) {
    data object ScreenAuth : Routes("auth_screen")
    data object ScreenNav : Routes("nav_screen")
}
