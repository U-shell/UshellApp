package ru.ushell.app.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.R
import ru.ushell.app.navigation.screen.SettingScreenDestination

@Composable
fun SettingScreen(
    navController: NavHostController,
){
    SettingContext(navController = navController)
}

@Composable
fun SettingContext(
    navController: NavHostController,
){
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .background(Color.White)
    ) {
        ListElement(navController = navController)
    }
}


@Composable
fun ListElement(
    navController: NavHostController
){
    Column{
        val screens = SettingScreenDestination.listSettingScreenDestination

        screens.forEach { screen ->
            ButtonDesign(
                screen = screen,
                navController = navController
            )
        }
    }
}

@Composable
fun ButtonDesign(
    screen: SettingScreenDestination,
    navController: NavHostController
){
    Box(
        modifier = Modifier
            .padding(
                end = 6.dp,
                top = 5.dp
            )
            .shadow(
                elevation = 5.dp,
                spotColor = Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
            .drawWithContent {
                val padding = 10.dp.toPx()
                clipRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height + padding
                ) {
                    this@drawWithContent.drawContent()
                }
            }
            .background(Color.White)
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .padding(
                    start = 5.dp,
                    end = 16.dp,
                    top = 21.dp,
                    bottom = 21.dp
                )
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row(
                modifier = Modifier
                    .padding(
                        start = 25.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ){
                Box{
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id=screen.icon!!),
                            contentDescription = null
                        )
                    }
                }
                Text(
                    text = screen.title!!,
                    color = Color.Black,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(
                            start = 15.dp
                        )
                )
            }
            Box(
                Modifier
                    .padding(
                        end = 6.dp
                    )
            ){
                Icon(
                    painter = painterResource(id = R.drawable.drawer_arrow),
                    contentDescription = null,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingScreenPreview(){
    val navController = rememberNavController()

    SettingScreen(navController)
}

@Preview(showBackground = true)
@Composable
fun ButtonDesignPreview() {
    ButtonDesign(
        SettingScreenDestination.Gadget,
        rememberNavController()
    )
}