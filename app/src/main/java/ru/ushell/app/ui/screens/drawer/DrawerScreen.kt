package ru.ushell.app.ui.screens.drawer

import ru.ushell.app.R

sealed class DrawerScreen (
    val route: String,
    val icon: Int,
    val title: String,
    val checkState: Boolean? = false
){
    data object EditProfile: DrawerScreen(
        route = "EditProfile",
        icon = R.drawable.drawer_edit_profile,
        title = "Профиль"
    )
    data object Noise: DrawerScreen(
        route = "Noise",
        icon = R.drawable.drawer_noise,
        title = "Уведомления",
        checkState = true
    )

    data object Gadget: DrawerScreen(
        route = "Gadget",
        icon = R.drawable.drawer_gadget,
        title = "Устройства"
    )

    data object Setting: DrawerScreen(
        route = "Setting",
        icon = R.drawable.drawer_setting,
        title = "Настройка"
    )

    data object Info: DrawerScreen(
        route = "info",
        icon = R.drawable.drawer_info,
        title = "Поддержка"
    )
}