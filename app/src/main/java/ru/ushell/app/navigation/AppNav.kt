package ru.ushell.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.R
import ru.ushell.app.navigation.screen.SettingNavHost
import ru.ushell.app.screens.messenger.ChatScreen
import ru.ushell.app.screens.profile.ProfileScreen
import ru.ushell.app.screens.schedule.ScheduleScreen
import ru.ushell.app.screens.setting.SettingScreen

sealed class ScreenDestination(
    val route: String,
    val icon: Int,
    val iconFocused: Int
) {

    companion object {
        private const val ROUTE_PROFILE = "route_profile"
        private const val ROUTE_SCHEDULE = "route_schedule"
        private const val ROUTE_MESSENGER = "route_messenger"
        private const val ROUTE_SETTING = "route_setting"

        val listScreenDestination = listOf(
            Profile,
            Schedule,
            Messenger,
            Setting
        )
    }

    data object Profile : ScreenDestination(
        route = ROUTE_PROFILE,
        icon = R.drawable.bottom_ic_profile,
        iconFocused = R.drawable.bottom_ic_profile_focused
    )

    data object Schedule : ScreenDestination(
        route = ROUTE_SCHEDULE,
        icon = R.drawable.bottom_ic_timetable,
        iconFocused = R.drawable.bottom_ic_timetable_focused
    )

    data object Messenger : ScreenDestination(
        route = ROUTE_MESSENGER,
        icon = R.drawable.bottom_ic_chat,
        iconFocused = R.drawable.bottom_ic_chat_focused
    )

    data object Setting : ScreenDestination(
        route = ROUTE_SETTING,
        icon = R.drawable.drawer_setting,
        iconFocused = R.drawable.drawer_setting
    )

}

@Composable
fun AppNavHost(
    gesturesEnabled: MutableState<Boolean>,
    bottomBarEnabled: MutableState<Boolean>,
    navController: NavHostController,
){
    gesturesEnabled.value = false

    NavHost(
        navController = navController,
        startDestination = ScreenDestination.Schedule.route
    ) {
        composable(ScreenDestination.Profile.route) {
            gesturesEnabled.value = true
            ProfileScreen()
        }

        composable(ScreenDestination.Schedule.route) {
            ScheduleScreen()
        }

        composable(ScreenDestination.Messenger.route) {
            ChatScreen(bottomBarEnabled = bottomBarEnabled)
        }

        composable(ScreenDestination.Setting.route) {
            val navController = rememberNavController()
            SettingNavHost(
                navController=navController,
                bottomBarEnabled=bottomBarEnabled
            )
        }
    }
}

