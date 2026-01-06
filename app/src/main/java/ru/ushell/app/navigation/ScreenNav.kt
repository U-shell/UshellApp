package ru.ushell.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.ui.theme.UshellBackground

@Composable
fun ScreenNav() {
    val navController = rememberNavController()
    val gesturesEnabled = remember { mutableStateOf(false) }
    val bottomBarEnabled = remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        bottomBar = {
            if (bottomBarEnabled.value) {
                BottomNavBar(
                    navController = navController
                )
            }
        },
        content = {
            Box {
                AppNavHost(
                    gesturesEnabled = gesturesEnabled,
                    bottomBarEnabled = bottomBarEnabled,
                    navController = navController
                )
            }
        }
    )

}


@Composable
fun BottomNavBar(
    navController: NavHostController
){
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    val shapes = 10.dp
    val screens = ScreenDestination.listScreenDestination

    Row(
        modifier = Modifier
            .clip(
                shape = RoundedCornerShape(
                    topStart = shapes,
                    topEnd = shapes
                )
            )
            .border(
                width = 1.5f.dp,
                color = Color.White,
                shape = RoundedCornerShape(
                    topStart = shapes,
                    topEnd = shapes
                )
            )
            .height(70.dp)
            .background(UshellBackground)
            .fillMaxWidth()
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        screens.forEach { screen ->
            ItemScreen(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun ItemScreen(
    screen: ScreenDestination,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val icon = if (selected) screen.iconFocused else screen.icon

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "icon",
                tint = Color.White
            )
        }
    }
}

@Composable
@Preview
fun ItemScreenPreview() {

    val navController = rememberNavController()
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    ItemScreen(
        screen = ScreenDestination.Schedule,
        currentDestination = currentDestination,
        navController = navController
    )
}

@Composable
@Preview
fun ScreenNavPreview(){
    ScreenNav()
}