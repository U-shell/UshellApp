package ru.ushell.app.old.ui.screens.drawer

import ru.ushell.app.R

sealed class Drawer (
    val route: String,
    val icon: Int? = 0,
    val title: String? = " ",
    val checkState: Boolean? = false
){
    data object DrawerContext: Drawer(
        route = "DrawerContext"
    )

    data object EditProfile: Drawer(
        route = "EditProfile",
        icon = R.drawable.drawer_edit_profile,
        title = "Профиль"
    )
    data object Noise: Drawer(
        route = "Noise",
        icon = R.drawable.drawer_noise,
        title = "Уведомления",
        checkState = true
    )

    data object Device: Drawer(
        route = "Gadget",
        icon = R.drawable.drawer_gadget,
        title = "Устройства"
    )

    data object Setting: Drawer(
        route = "Setting",
        icon = R.drawable.drawer_setting,
        title = "Настройка"
    )

    data object Info: Drawer(
        route = "info",
        icon = R.drawable.drawer_info,
        title = "Поддержка"
    )
}