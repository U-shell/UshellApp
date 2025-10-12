package ru.ushell.app.screens.profile.drawer

import ru.ushell.app.R

sealed class Drawer (
    val route: String,
    val icon: Int? = 0,
    val title: String? = " ",
){

    data object Device: Drawer(
        route = "Gadget",
        icon = R.drawable.drawer_gadget,
        title = "Устройства"
    )

}