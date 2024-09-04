package ru.ushell.app.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.ushell.app.ui.screens.analyticScreen.AnalyticScreen
import ru.ushell.app.ui.screens.chatScreen.ChatScreen
import ru.ushell.app.ui.screens.drawer.ModalDrawer
import ru.ushell.app.ui.screens.profileScreen.ProfileScreen
import ru.ushell.app.ui.screens.roomScreen.RoomScreen
import ru.ushell.app.ui.screens.startScreen.StartScreen
import ru.ushell.app.ui.screens.timeTable.TimeTableScreen
import ru.ushell.app.ui.theme.UshellAppTheme

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    drawerState: DrawerState,
    gesturesEnabled: MutableState<Boolean>,
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Profile.route
    ) {

        composable(route = BottomBarScreen.Profile.route) {
            UshellAppTheme {
                ProfileScreen(
                    drawerState=drawerState,
                )
                gesturesEnabled.value = true
            }
        }

        composable(route = BottomBarScreen.Room.route) {
            UshellAppTheme {
                RoomScreen()
                gesturesEnabled.value = false
            }
        }

        composable(route = BottomBarScreen.TimeTable.route) {
            UshellAppTheme {
                TimeTableScreen()
                gesturesEnabled.value = false
            }
        }

        composable(route = BottomBarScreen.Chat.route) {
            UshellAppTheme {
                ChatScreen()
                gesturesEnabled.value = false
            }
        }

        composable(route = BottomBarScreen.Analytic.route) {
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
    content: @Composable () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = RoutesExit.DialogExit.route
    ) {

        composable(RoutesExit.DialogExit.route) {
            ModalDrawer(
                navController = navController,
                drawerState = drawerState,
                gesturesEnabled = gesturesEnabled,
                content = content
            )
        }

        composable(RoutesExit.StartScreen.route) {
            StartScreen()
        }
    }
}
