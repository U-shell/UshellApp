package ru.ushell.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.screens.SplashScreen
import ru.ushell.app.screens.auth.AuthorizeScreen

sealed class Destination(val route: String) {

    data object Splash : Destination(ROUTE_SPLASH)
    data object Auth : Destination(ROUTE_AUTH)
    data object Inside : Destination(ROUTE_INSIDE)

    companion object {
        private const val ROUTE_SPLASH = "route_splash"
        private const val ROUTE_AUTH = "route_auth"
        private const val ROUTE_INSIDE = "route_inside"
    }

}

@Composable
fun MainNavHost(
    isLoggedIn: Boolean
){
    val navController = rememberNavController()
    val route = if (isLoggedIn) Destination.Inside.route else Destination.Splash.route

    NavHost(
        navController = navController,
        startDestination = route
    ){
        composable(Destination.Splash.route) {
            SplashScreen {
                navController.navigate(
                    if (isLoggedIn) Destination.Inside.route else Destination.Auth.route
                ) {
                    popUpTo(Destination.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(Destination.Auth.route) {
            AuthorizeScreen(
                navController = navController
            )
        }

        composable(Destination.Inside.route) {
            ScreenNav()
        }
    }
}
