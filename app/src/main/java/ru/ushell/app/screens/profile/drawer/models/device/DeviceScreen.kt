package ru.ushell.app.screens.profile.drawer.models.device

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.R
import ru.ushell.app.navigation.DrawerRoutes
import ru.ushell.app.screens.profile.drawer.models.device.qrscanner.QRScannerScreen
import ru.ushell.app.ui.theme.ChatIFBackground
import ru.ushell.app.ui.theme.DrawerBorderColor


@Composable
fun DeviceScreen(
    navController: NavHostController,
    onBottomBarVisibilityChange: (Boolean) -> Unit,
) {
    val deviceNavController = rememberNavController()
    val currentRoute by deviceNavController.currentBackStackEntryAsState()

    LaunchedEffect(currentRoute?.destination?.route) {
        val showBottomBar = currentRoute?.destination?.route == RoutesDevice.ScreenDevice.route
        onBottomBarVisibilityChange(showBottomBar)
    }

    NavHost(
        navController = deviceNavController,
        startDestination = RoutesDevice.ScreenDevice.route
    ) {
        composable(RoutesDevice.ScreenDevice.route) {
            DeviceMainScreen(
                parentNavController = navController,
                deviceNavController = deviceNavController
            )
        }
        composable(RoutesDevice.ScreenQR.route) {
            QRScannerScreen(navControllerDevice = deviceNavController)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceMainScreen(
    parentNavController: NavHostController,
    deviceNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Устройства") },
                navigationIcon = {
                    IconButton(onClick = { parentNavController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.drawer_icon_arrow_exit),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QRPanel(deviceNavController = deviceNavController)
            MePanel()
        }
    }
}

@Composable
fun QRPanel(deviceNavController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(vertical = 24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_screen_logo),
            contentDescription = "App logo",
            modifier = Modifier.size(158.dp)
        )

        Text(
            text = "Можно добавить устройство",
            color = Color.White,
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = { deviceNavController.navigate(RoutesDevice.ScreenQR.route) },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ChatIFBackground),
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.drawer_icon_qr),
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Подключить устройство", color = Color.White)
        }
    }
}

@Composable
fun MePanel() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bottom_ic_profile),
            contentDescription = "This device",
            modifier = Modifier
                .size(70.dp)
                .border(BorderStroke(2.dp, DrawerBorderColor), CircleShape)
                .padding(2.dp)
                .clip(CircleShape)
                .background(Color.Black)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text("ThisDevice")
            Text("Version")
            Text("Location")
        }
    }
}



sealed class RoutesDevice(val route: String) {
    object ScreenDevice : RoutesDevice("device_device")
    object ScreenQR : RoutesDevice("device_qr")
}

@Preview
@Composable
fun DeviceScreenPreview(){
    val navController = rememberNavController()
    val bottomBarEnabled = remember { mutableStateOf(true) }
    DeviceScreen(
        navController=navController,
        onBottomBarVisibilityChange= { bottomBarEnabled }
    )
}
