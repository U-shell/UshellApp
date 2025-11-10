package ru.ushell.app.screens.profile.drawer.models.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.R
import ru.ushell.app.screens.profile.drawer.Drawer
import ru.ushell.app.screens.profile.drawer.models.TopNavDrawerPanel
import ru.ushell.app.screens.profile.drawer.models.device.qrscanner.QRScannerScreen
import ru.ushell.app.screens.utils.TypeScanner
import ru.ushell.app.ui.theme.ChatIFBackground
import ru.ushell.app.ui.theme.DeviceLocation
import ru.ushell.app.ui.theme.DeviceNameTitle
import ru.ushell.app.ui.theme.DeviceThisDevice
import ru.ushell.app.ui.theme.DeviceVersionApp


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
            DeviceContext(
                parentNavController = navController,
                deviceNavController = deviceNavController
            )
        }
        composable(RoutesDevice.ScreenQR.route) {
            QRScannerScreen(navControllerDevice = deviceNavController, typeScanner = TypeScanner.LOGIN)
        }
    }
}

@Composable
fun DeviceContext(
    parentNavController: NavHostController,
    deviceNavController: NavHostController,
){
    val backgroundColorItem = Color(0xFF181818)
    Column (
        modifier = Modifier
            .fillMaxSize()
                .background(Color(0xFF000000))
    ){
        TopNavDrawerPanel(
            text = stringResource(R.string.device),
            route = Drawer.Device.route,
            navController = parentNavController,
            modifier = Modifier
                .background(Color(0xFF232325))

        )

        QrScannerPanel(
            deviceNavController = deviceNavController,
            modifier = Modifier
                .background(backgroundColorItem)

        )
        Spacer(modifier = Modifier.height(15.dp))

        ThisDevicePanel(
            modifier = Modifier
                .background(backgroundColorItem)
        )
        Spacer(modifier = Modifier.height(15.dp))

        OtherDevicePanel(
            modifier = Modifier
                .background(backgroundColorItem)
        )
    }

}

@Composable
fun QrScannerPanel(
    deviceNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp
            )
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Image(
            painter = painterResource(id = R.drawable.qr_laptop),
            contentDescription = null,
            modifier = Modifier.size(158.dp)
        )

        Text(
            text = stringResource(R.string.qrBrief),
            fontSize = 15.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(
                    start = 30.dp,
                    end = 30.dp,
                    top = 10.dp,
                    bottom = 10.dp
                )
        )

        Button(
            onClick = { deviceNavController.navigate(RoutesDevice.ScreenQR.route) },
            colors = ButtonDefaults.buttonColors(containerColor = ChatIFBackground),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){

                Icon(
                    painter = painterResource(id = R.drawable.drawer_icon_qr),
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.addDevice),
                    fontSize = 15.sp,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun DeviceItem(
    nameDevice: String,
    versionApp: String,
    locationDevice: String,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Icon(
            painter = painterResource(id = R.drawable.drawer_gadget),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp
                )
            ,
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = nameDevice,
                style = DeviceNameTitle
            )
            Text(
                text = versionApp,
                style = DeviceVersionApp
            )
            Text(
                text = locationDevice,
                style = DeviceLocation
            )
        }
    }
}

@Composable
fun ThisDevicePanel(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.deviceThis),
                style = DeviceThisDevice
            )
        }

        DeviceItem(
            nameDevice = "d",
            versionApp = "d",
            locationDevice = "d"
        )

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFFB72E2E),
            ),
            shape = RoundedCornerShape(0.dp),
            contentPadding = PaddingValues(0.dp),

            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.device_exit),
                    contentDescription = null,
                    modifier = Modifier
                        .size(
                            width = 60.dp,
                            height = 30.dp)
                )
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.deviceClousSession),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }


}

@Composable
fun OtherDevicePanel(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .padding(10.dp)
        ){
            Text(
                text = stringResource(id = R.string.deviceОther),
                style = DeviceThisDevice
            )
        }

        DeviceItem(
            nameDevice = "d",
            versionApp = "d",
            locationDevice = "d"
        )
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

@Preview
@Composable
fun QrScannerPanelPreview(){
    QrScannerPanel(rememberNavController())
}

@Preview
@Composable
fun DeviceItemPreview(){
    DeviceItem(
        nameDevice = "nameDevice",
        versionApp = "versionApp",
        locationDevice = "LocationDevice"
    )
}

@Preview
@Composable
fun ThisDevicePanelPreview(){
    ThisDevicePanel()
}
