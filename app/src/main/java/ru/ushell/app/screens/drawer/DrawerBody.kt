package ru.ushell.app.old.ui.screens.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import ru.ushell.app.R

@Composable
fun DrawerBody(
    navController: NavHostController
){
    RowButton(navController = navController)
}

@Composable
fun RowButton(
    navController: NavHostController
){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Column{
            val screens = listOf(
                Drawer.EditProfile,
                Drawer.Noise,
                Drawer.Device,
            )
            screens.forEach { screen ->
                ButtonDesign(
                    screen = screen,
                    navController = navController
                )
            }
        }
        Column{
            Box(modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
            ){
                Text(
                    text = "Support",
                    fontSize = 20.sp
                )
            }
        }
        Column{
            val screens = listOf(
                Drawer.Setting,
                Drawer.Info
            )
            screens.forEach { screen ->
                ButtonDesign(
                    screen = screen,
                    navController = navController
                )
            }
        }

    }
}

@Composable
fun ButtonDesign(
    screen: Drawer,
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
                if(screen.checkState == true) {
                    val checkState = remember { mutableStateOf(false) }
                    Switch(
                        checked = checkState.value,
                        onCheckedChange = {checkState.value = it}
                    )
                }else{
                    Icon(
                        painter = painterResource(id = R.drawable.drawer_arrow),
                        contentDescription = null,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
