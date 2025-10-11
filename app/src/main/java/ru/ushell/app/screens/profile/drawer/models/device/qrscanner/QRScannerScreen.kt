package ru.ushell.app.screens.profile.drawer.models.device.qrscanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.ushell.app.navigation.DrawerRoutes
import ru.ushell.app.old.ui.screens.drawer.models.TopNavPanel

@Composable
fun QRScannerScreen(
    navControllerDevice: NavHostController,
) {
    QRScannerContext(
        navController=navControllerDevice
    )
}

@Composable
fun QRScannerContext(
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFE7E7E7)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (topPanel, scanner) = createRefs()

            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .systemBarsPadding()
                    .constrainAs(topPanel) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            ) {
                TopNavPanel(
                    text = "QR",
                    screen = DrawerRoutes.StartScreen.route,
                    navController = navController,
                    modifier = Modifier.background(Color.Transparent)
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(scanner) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxSize()
            ) {
                CameraScreen(
                    navController = navController,
                )
            }
        }
    }
}


@Preview
@Composable
fun QRScannerPreview() {
    val navController = rememberNavController()
    QRScannerScreen(
        navControllerDevice=navController
    )
}