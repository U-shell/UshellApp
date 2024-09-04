package ru.ushell.app.ui.navigation

sealed class RoutesExit(
    val route: String,
) {
    data object DialogExit: RoutesExit(
        route = "drawer_drawer"
    )
    data object StartScreen : RoutesExit(
        route = "drawer_start"
    )
}
