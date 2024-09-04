package ru.ushell.app.ui.navigation

import ru.ushell.app.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val iconFocused: Int
) {

    data object Profile: BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon = R.drawable.bottom_ic_profile,
        iconFocused = R.drawable.bottom_ic_profile_focused
    )

    data object Room: BottomBarScreen(
        route = "room",
        title = "Room",
        icon = R.drawable.bottom_ic_room,
        iconFocused = R.drawable.bottom_ic_room_focused
    )

    data object TimeTable: BottomBarScreen(
        route = "timetable",
        title = "TimeTable",
        icon = R.drawable.bottom_ic_timetable,
        iconFocused = R.drawable.bottom_ic_timetable_focused
    )

    data object Chat: BottomBarScreen(
        route = "chat",
        title = "Chat",
        icon = R.drawable.bottom_ic_chat,
        iconFocused = R.drawable.bottom_ic_chat_focused
    )

    data object Analytic: BottomBarScreen(
        route = "analytic",
        title = "Analytic",
        icon = R.drawable.bottom_ic_analytic,
        iconFocused = R.drawable.bottom_ic_analytic_focused
    )

}
