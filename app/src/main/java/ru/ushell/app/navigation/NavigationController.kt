package ru.ushell.app.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.ushell.app.screens.FirstScreen
import ru.ushell.app.screens.Routes
import ru.ushell.app.screens.messenger.ChatScreen
import ru.ushell.app.screens.profile.ProfileScreen
import ru.ushell.app.screens.profile.drawer.Drawer
import ru.ushell.app.screens.profile.drawer.DrawerScreen
import ru.ushell.app.screens.profile.drawer.models.device.DeviceScreen
import ru.ushell.app.screens.timetable.TimeTableScreen
import ru.ushell.app.ui.theme.UshellAppTheme

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    drawerState: DrawerState,
    gesturesEnabled: MutableState<Boolean>,
    bottomBarEnabled: MutableState<Boolean>,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationBottomBar.TimeTable.route
    ) {

        composable(route = NavigationBottomBar.Profile.route) {
            UshellAppTheme {
                ProfileScreen(
                    drawerState = drawerState,
                )
                gesturesEnabled.value = true
            }
        }
        composable(route = NavigationBottomBar.TimeTable.route) {
            UshellAppTheme {
                TimeTableScreen()
                gesturesEnabled.value = false
            }
        }
        composable(route = NavigationBottomBar.Chat.route) {
            UshellAppTheme{
                ChatScreen(
                    bottomBarEnabled = bottomBarEnabled
                )
                gesturesEnabled.value = false
            }
        }
    }
}

@Composable
fun DrawerNavController(
    drawerState: DrawerState,
    navController: NavHostController,
    gesturesEnabled: MutableState<Boolean>,
    bottomBarEnabled: MutableState<Boolean>,

    content: @Composable () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = DrawerRoutes.DialogScreen.route
    ) {

        composable(DrawerRoutes.DialogScreen.route) {
            DrawerScreen(
                navController = navController,
                drawerState = drawerState,
                gesturesEnabled = gesturesEnabled,
                content = content
            )
        }
        composable(DrawerRoutes.ScreenNav.route) {
            UshellAppTheme {
                ScreenNav()
            }
        }
        composable(DrawerRoutes.StartScreen.route) {
            FirstScreen({
                navController.navigate(Routes.Auth.route)
            })
        }

        composable(route = Drawer.Device.route ) {
            DeviceScreen(
                navController = navController,
                onBottomBarVisibilityChange = {bottomBarEnabled}
            )
        }
    }
}
