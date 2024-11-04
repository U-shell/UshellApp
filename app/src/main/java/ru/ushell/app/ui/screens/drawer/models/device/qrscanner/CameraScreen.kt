package ru.ushell.app.ui.screens.drawer.models.device.qrscanner

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.zxing.ResultPoint
import ru.ushell.app.api.Config
import ru.ushell.app.api.websocket.chat.ChatDeliver
import ru.ushell.app.api.websocket.qr.QRCodeConnect
import ru.ushell.app.api.websocket.qr.QRCodeDeliver
import ru.ushell.app.models.User
import ru.ushell.app.ui.screens.drawer.models.device.RoutesDevice
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.log


@Composable
fun CameraScreen(
    navController: NavHostController,
){
    var code by remember { mutableStateOf("") }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var showFrame by remember { mutableStateOf(false) }
    var qrCodeCoordinates by remember { mutableStateOf<Array<ResultPoint>?>(null) }
    var previewViewH by remember { mutableIntStateOf(0) }
    var previewViewW by remember { mutableIntStateOf(0) }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        var hasCameraPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                hasCameraPermission = granted
            }
        )

        LaunchedEffect(key1 = true) {
            launcher.launch(Manifest.permission.CAMERA)
        }
        var viewSize by remember { mutableStateOf(Size.Zero) }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (hasCameraPermission) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
//                        .onSizeChanged {
//                            viewSize = Size(it.width.toFloat(), it.height.toFloat())
//                        }
//                        .drawWithContent {
//                            drawContent()
//                            // Проверяем, есть ли координаты QR-кода и должна ли показываться рамка
//                            if (qrCodeCoordinates != null && showFrame) {
//
//                                val points = qrCodeCoordinates!!
//
//                                val topLeft = Offset(points[0].x,points[0].y)
//                                val topRight = Offset(points[1].x,points[1].y)
//                                val bottomRight = Offset(points[2].x,points[2].y)
//                                val bottomLeft = Offset(points[3].x,points[3].y)
//
//                                // Создаем Path для рисования рамки
//                                val path = Path().apply {
//                                    moveTo(topLeft.x, topLeft.y)
//                                    lineTo(topRight.x, topRight.y)
//                                    lineTo(bottomRight.x, bottomRight.y)
//                                    lineTo(bottomLeft.x, bottomLeft.y)
//                                    close()
//                                }
//
//                                drawPath(
//                                    path = path,
//                                    color = Color.White,
//                                    style = Stroke(width = 4.dp.toPx())
//                                )
//                            }
//                        }
                    ,
                    factory = { context ->
                        val previewView = PreviewView(context)
                        val preview = Preview.Builder().build()


                        val selector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()

                        preview.setSurfaceProvider(previewView.surfaceProvider)

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QRCodeAnalyzer(
                                onQrCodeScanned = { result ->
                                    result?.let {
                                        code = it
                                    }
                                    if (result != null) {
                                        showFrame = true
                                    }
                                },
                                onQrCodeCoordinate = { coordinate ->
                                    qrCodeCoordinates = coordinate
                                }
                            )
                        )


                        try {
                            cameraProviderFuture.get().bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        return@AndroidView previewView
                    }
                )
            }
        }
    }

    ConnectDevice(
        code = code,
        navController = navController
    )
}


fun ConnectDevice(
    code: String,
    navController: NavHostController,
){
    if(code != "") {
        QRCodeConnect().connect()

        val qrCodeDeliver =
            QRCodeDeliver(
                code
            )

        qrCodeDeliver.connect(Config.webSocketAddressQR)

        navController.navigate(RoutesDevice.ScreenDevice.route)

        qrCodeDeliver.disconnect()
        QRCodeConnect().disconnect()
    }
}
