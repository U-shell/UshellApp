package ru.ushell.app.ui.screens.drawer.models.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.R
import ru.ushell.app.ui.navigation.DrawerRoutes
import ru.ushell.app.ui.screens.chatScreen.ChatContext
import ru.ushell.app.ui.screens.chatScreen.RoutesChat
import ru.ushell.app.ui.screens.chatScreen.dialog.DialogScreen
import ru.ushell.app.ui.screens.drawer.Drawer
import ru.ushell.app.ui.screens.drawer.models.TopNavPanel
import ru.ushell.app.ui.screens.drawer.models.device.qrscanner.QRScannerScreen
import ru.ushell.app.ui.theme.ChatIFBackground
import ru.ushell.app.ui.theme.DrawerExitButtonBackgroundColor
import ru.ushell.app.ui.theme.NoSystemBarColorTheme
import ru.ushell.app.ui.theme.SystemBarColorTheme
import ru.ushell.app.ui.theme.UshellAppTheme

@Composable
fun DeviceScreen(
    navController: NavHostController,
    bottomBarEnabled: MutableState<Boolean>,
){
    val navControllerDevice = rememberNavController()
    DeviceNav(
        navController=navController,
        navControllerDevice=navControllerDevice,
        bottomBarEnabled=bottomBarEnabled
    )
}

@Composable
fun DeviceNav(
    navController: NavHostController,
    navControllerDevice: NavHostController,
    bottomBarEnabled: MutableState<Boolean>,
){
    NavHost(
        navController = navControllerDevice,
        startDestination = RoutesDevice.ScreenDevice.route
    ) {
        composable(RoutesDevice.ScreenDevice.route) {
            bottomBarEnabled.value = true
            SystemBarColorTheme {
                DeviceContext(
                    navController=navController,
                    navControllerDevice=navControllerDevice,
                    bottomBarEnabled=bottomBarEnabled
                )
            }
        }
        composable(RoutesDevice.ScreenQR.route) {
            bottomBarEnabled.value = false
            NoSystemBarColorTheme{
                QRScannerScreen(
                    navControllerDevice=navControllerDevice,
                )
            }
        }
    }
}

@Composable
fun DeviceContext(
    navController: NavHostController,
    navControllerDevice: NavHostController,
    bottomBarEnabled: MutableState<Boolean>,
) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .background(Color.White)
            .fillMaxSize()
    ){
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            topBar = {
                bottomBarEnabled.value=true
                TopNavPanel(
                    text = "Устройства",
                    screen = DrawerRoutes.ScreenNav.route,
                    navController=navController,
                    modifier = Modifier
                        .background(color = Color.Black)
                )
            },
            contentWindowInsets = ScaffoldDefaults
                .contentWindowInsets
                .exclude(WindowInsets.navigationBars)
                .exclude(WindowInsets.ime)
        ){paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QRPanel(
                    navControllerDevice=navControllerDevice
                )
            }
        }
    }
}

@Composable
fun QRPanel(
    navControllerDevice:NavHostController
){
    Box(
        modifier = Modifier
            .padding(
                top = 10.dp,
                bottom = 10.dp
            )
            .background(Color.DarkGray)
            .fillMaxWidth(),
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(){
                Image(
                    painterResource(id = R.drawable.splash_screen_logo),
                    contentDescription ="splash_screen_logo",
                    modifier = Modifier
                        .size(158.dp)
                )
            }
            Box(
                modifier = Modifier
                    .padding(top=10.dp)
            ) {
                Text(
                    text ="можнодобавить устройство ",
                    color = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
            ){
                Button(
                    onClick = {
                        navControllerDevice.navigate(RoutesDevice.ScreenQR.route)
                    },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ChatIFBackground
                    ),
                ) {
                    Box(
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.drawer_icon_qr),
                            contentDescription = null,
                            modifier = Modifier,
                            tint = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 5.dp)
                    ){
                        Text(
                            text = "Подключить устройство"
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun MePanel(){

}

@Composable
fun SessionPanel(){

}

sealed class RoutesDevice(
    val route: String,
) {
    data object ScreenDevice : RoutesDevice("device_device")
    data object ScreenQR : RoutesDevice("device_qr")
}

@Preview
@Composable
fun DeviceScreenPreview(){
    val navController = rememberNavController()
    val bottomBarEnabled = remember { mutableStateOf(true) }
    DeviceScreen(
        navController=navController,
        bottomBarEnabled=bottomBarEnabled
    )
}
