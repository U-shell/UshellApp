package ru.ushell.app.old.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.ushell.app.old.ui.screens.drawer.Drawer
import ru.ushell.app.old.ui.screens.drawer.models.editProfile.EditProfileScreen
import ru.ushell.app.old.ui.screens.drawer.models.noise.NoiseScreen
import ru.ushell.app.old.ui.screens.drawer.models.settings.SettingScreen
import ru.ushell.app.old.ui.screens.drawer.models.support.SupportScreen
import ru.ushell.app.screens.StartScreen
import ru.ushell.app.screens.analyticScreen.AnalyticScreen
import ru.ushell.app.screens.navigation.DrawerRoutes
import ru.ushell.app.screens.navigation.NavigationBottomBar
import ru.ushell.app.screens.navigation.ScreenNav
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
//            UshellAppTheme {
//                ProfileScreen(
//                    drawerState = drawerState,
//                )
//                gesturesEnabled.value = true
//            }
        }

        composable(route = NavigationBottomBar.Room.route) {
//            UshellAppTheme {
//                RoomScreen()
//                gesturesEnabled.value = false
//            }
        }

        composable(route = NavigationBottomBar.TimeTable.route) {
            UshellAppTheme {
                TimeTableScreen()
                gesturesEnabled.value = false
            }
        }
        composable(route = NavigationBottomBar.Chat.route) {
            UshellAppTheme{
//                ChatScreen(
//                    bottomBarEnabled = bottomBarEnabled
//                )
                gesturesEnabled.value = false
            }
        }

        composable(route = NavigationBottomBar.Analytic.route) {
            UshellAppTheme {
                AnalyticScreen()
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
//            DrawerScreen(
//                navController = navController,
//                drawerState = drawerState,
//                gesturesEnabled = gesturesEnabled,
//                content = content
//            )
        }
        composable(DrawerRoutes.ScreenNav.route) {
                ScreenNav()
        }
        composable(DrawerRoutes.StartScreen.route) {
            StartScreen()
        }
        composable(route = Drawer.EditProfile.route) {
            UshellAppTheme {
                EditProfileScreen()
            }
        }
        composable(route = Drawer.Noise.route) {
            UshellAppTheme{
                NoiseScreen()
            }
        }
        composable(route = Drawer.Device.route ) {
//            DeviceScreen(
//                navController = navController,
//                bottomBarEnabled = bottomBarEnabled
//            )
        }
        composable(route = Drawer.Setting.route ) {
            UshellAppTheme{
                SettingScreen()
            }
        }
        composable(route = Drawer.Info.route ) {
            UshellAppTheme {
               SupportScreen()
            }
        }
    }
}
