package ru.ushell.app.navigation

sealed class DrawerRoutes(
    val route: String,
) {
    data object DialogScreen: DrawerRoutes(
        route = "drawer_drawer"
    )
    data object StartScreen : DrawerRoutes(
        route = "drawer_start"
    )
    data object ScreenNav : DrawerRoutes(
        route = "drawer_screenNav"
    )
}
