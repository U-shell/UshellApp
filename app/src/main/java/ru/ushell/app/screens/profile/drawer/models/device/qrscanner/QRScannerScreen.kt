package ru.ushell.app.screens.profile.drawer.models.device.qrscanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.R
import ru.ushell.app.navigation.DrawerRoutes
import ru.ushell.app.screens.profile.drawer.models.TopNavDrawerPanel
import ru.ushell.app.screens.utils.TypeScanner

@Composable
fun QRScannerScreen(
    navControllerDevice: NavHostController,
    typeScanner: TypeScanner,
) {
    QRScannerContext(
        navController=navControllerDevice,
        typeScanner=typeScanner,
    )
}

@Composable
fun QRScannerContext(
    navController: NavHostController,
    typeScanner: TypeScanner,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (topPanel) = createRefs()

            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .systemBarsPadding()
                    .constrainAs(topPanel) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            ) {
                TopNavDrawerPanel(
                    text = stringResource(R.string.scannerQR),
                    route = DrawerRoutes.StartScreen.route,
                    navController = navController,
                    modifier = Modifier.background(Color.Transparent)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CameraScreen(
                    navController = navController,
                    typeScanner = typeScanner,
                )
            }
        }
    }
}

//
//@Preview
//@Composable
//fun QRScannerPreview() {
//    val navController = rememberNavController()
//    QRScannerScreen(
//        navControllerDevice=navController,
//    )
//}