package ru.ushell.app.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.models.User
import ru.ushell.app.ui.theme.UshellBackground


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenNav() {
    val navController = rememberNavController()

    val navControllerDrawer = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val gesturesEnabled = remember { mutableStateOf(false) }
    val bottomBarEnabled = remember { mutableStateOf(true) }
//TODO: сделать переход по свайту в стороны
    DrawerNavController(
        navController = navControllerDrawer,
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        bottomBarEnabled = bottomBarEnabled
    ) {
        Scaffold(
            modifier = Modifier
                .navigationBarsPadding(),
            bottomBar = {
                if (bottomBarEnabled.value) {
                    BottomBar(
                        navController = navController
                    )
                }
            },
            content = {
                Box {
                    BottomNavGraph(
                        navController = navController,
                        drawerState = drawerState,
                        gesturesEnabled = gesturesEnabled,
                        bottomBarEnabled = bottomBarEnabled
                    )
                }
            }
        )
    }
}

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val screens = listOf(
        NavigationBottomBar.Profile,
        NavigationBottomBar.Room,
        NavigationBottomBar.TimeTable,
        NavigationBottomBar.Chat,
        NavigationBottomBar.Analytic
    )

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination
    val shapes = 10.dp

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
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}


@Composable
fun AddItem(
    screen: NavigationBottomBar,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val background = UshellBackground
//  цвет у выделиных вкладках
//        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.Transparent

    val contentColor = Color.White
//        if (selected) Color.White else Color.Black //смена цвета на неактивых сценах

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background)
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
            if(screen.title == "Report"){
//                BadgedBox(badge = { Badge { Text("6") } }) {
                    Icon(
                        painter = painterResource(id = if (selected) screen.iconFocused else screen.icon),
                        contentDescription = "icon",
                        tint = contentColor
                    )
//                }
            }else{
                Icon(
                    painter = painterResource(id = if (selected) screen.iconFocused else screen.icon),
                    contentDescription = "icon",
                    tint = contentColor
                )
            }
            // появление текста рядом с иконкой
//            AnimatedVisibility(visible = selected) {
//                Text(
//                    text = screen.title,
//                    color = contentColor
//                )
//            }
        }
    }
}

@Composable
@Preview
fun ScreenNavPreview() {
    User.getInstance(LocalContext.current)
    ScreenNav()
}

@Composable
@Preview
fun BottomNavPreview() {
    val navController = rememberNavController()
    BottomBar(navController = navController)
}
