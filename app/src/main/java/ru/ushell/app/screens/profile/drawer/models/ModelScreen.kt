package ru.ushell.app.screens.profile.drawer.models

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.R

@Composable
fun TopNavDrawerPanel(
    text: String,
    route: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(
                top = 5.dp,
                bottom = 10.dp
            )
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Box(
                modifier = Modifier
                    .padding(start = 10.dp)
            ){
                IconButton(
                   onClick = {
                       navController.navigate(route)
                   },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.drawer_icon_arrow_exit),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .weight(1f)
            ){
                Text(
                    text = text,
                    fontSize = 25.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun TopNavPanelPreview() {
    val navController = rememberNavController()

    TopNavDrawerPanel(
        text = "Title",
        route = "Drawer.Device",
        navController=navController,
        modifier = Modifier
    )
}
