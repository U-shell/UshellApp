package ru.ushell.app.navigation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.ushell.app.R
import ru.ushell.app.screens.setting.SettingScreen
import ru.ushell.app.screens.setting.items.device.DeviceScreen


sealed class SettingScreenDestination(
    val route: String,
    val icon: Int? = 0,
    val title: String? = " ",
) {

    companion object {
        private const val ROUTE_SETTING = "route_setting"
        private const val ROUTE_GADGET = "route_setting_gadget"

        val listSettingScreenDestination = listOf(
            Gadget,
        )
    }

    data object Setting : SettingScreenDestination(ROUTE_SETTING)

    data object Gadget: SettingScreenDestination(
        route = ROUTE_GADGET,
        icon = R.drawable.drawer_gadget,
        title = "Устройства"
    )

}


@Composable
fun SettingNavHost(
    navController: NavHostController,
    bottomBarEnabled: MutableState<Boolean>,
){
    NavHost(
        navController = navController,
        startDestination = SettingScreenDestination.Setting.route
    ){
        composable(SettingScreenDestination.Setting.route) {
            bottomBarEnabled.value = true
            SettingScreen(navController = navController)
        }

        composable(SettingScreenDestination.Gadget.route) {
            bottomBarEnabled.value = false
            DeviceScreen(navController = navController)
        }
    }
}