//package ru.ushell.app.old.ui.screens.drawer.models.device.qrscanner
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.graphics.Rect
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.drawscope.translate
//import androidx.compose.ui.layout.onGloballyPositioned
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.unit.IntSize
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.LifecycleOwner
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.google.zxing.BinaryBitmap
//import com.google.zxing.MultiFormatReader
//import com.google.zxing.RGBLuminanceSource
//import com.google.zxing.ResultPoint
//import com.google.zxing.common.HybridBinarizer
//import ru.ushell.app.R
//import ru.ushell.app.api.Config
//import ru.ushell.app.api.websocket.qr.QRCodeConnect
//import ru.ushell.app.api.websocket.qr.QRCodeDeliver
//import ru.ushell.app.ui.screens.drawer.models.device.RoutesDevice
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//@Composable
//fun CameraScreen(navController: NavHostController) {
//    var code by remember { mutableStateOf("") }
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
//    var qrCodeCoordinates by remember { mutableStateOf<Array<ResultPoint>?>(null) }
//    var previewViewSize by remember { mutableStateOf(IntSize.Zero) }
//    var imageSize by remember { mutableStateOf(Size.Zero) }
//    var qrCodeBounds by remember { mutableStateOf<Rect?>(null) }
//
//    Surface(modifier = Modifier.fillMaxSize()) {
//        var hasCameraPermission by remember {
//            mutableStateOf(
//                ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.CAMERA
//                ) == PackageManager.PERMISSION_GRANTED
//            )
//        }
//
//        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
//            hasCameraPermission = granted
//        }
//
//        LaunchedEffect(key1 = true) {
//            launcher.launch(Manifest.permission.CAMERA)
//        }
//
//        Column(modifier = Modifier.fillMaxSize()) {
//            if (hasCameraPermission) {
//                Box(modifier = Modifier.fillMaxSize()) {
//                    // PreviewView для камеры
//                    AndroidView(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .onGloballyPositioned { layoutCoordinates ->
//                                previewViewSize = layoutCoordinates.size
//                            },
//                        factory = { ctx ->
//                            val previewView = PreviewView(ctx).apply {
//                                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
//                            }
//                            val preview = Preview.Builder().build()
//
//                            val selector = CameraSelector.Builder()
//                                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                                .build()
//
//                            preview.surfaceProvider = previewView.surfaceProvider
//
//                            val imageAnalysis = ImageAnalysis.Builder()
//                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                                .build()
//
//                            imageAnalysis.setAnalyzer(
//                                ContextCompat.getMainExecutor(ctx),
//                                QRCodeAnalyzer(
//                                    onQrCodeScanned = { result ->
//                                        result?.let {
//                                            code = it
//                                        }
//                                    },
//                                    onQrCodeCoordinate = { coordinates, size ->
//                                        qrCodeCoordinates = coordinates
//                                        imageSize = size
//                                    }
//                                )
//                            )
//
//                            try {
//                                cameraProviderFuture.get().bindToLifecycle(lifecycleOwner, selector, preview, imageAnalysis)
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            }
//
//                            return@AndroidView previewView
//                        }
//                    )
//
//                    // Canvas для рисования рамки
//                    if (qrCodeCoordinates != null && previewViewSize.width > 0 && previewViewSize.height > 0) {
////                        DrawQRCodeFrame(qrCodeCoordinates!!, imageSize, previewViewSize)
//                    }
//                }
//            }
//        }
//    }
//    ConnectDevice(code, navController)
//}
//
//@Composable
//fun DrawQRCodeFrame(
//    qrCodeCoordinates: Array<ResultPoint>?,
//    imageSize: Size, // Размер изображения с камеры
//    previewViewSize: IntSize // Размер экрана в пикселях
//) {
//    Canvas(modifier = Modifier.fillMaxSize()) {
//        if (qrCodeCoordinates != null && qrCodeCoordinates.size >= 3) {
//            val topLeft = qrCodeCoordinates[0]
//            val topRight = qrCodeCoordinates[1]
//            val bottomRight = qrCodeCoordinates[2]
//            val bottomLeft = if (qrCodeCoordinates.size == 4) {
//                qrCodeCoordinates[3]
//            } else {
//                ResultPoint((topLeft.x + bottomRight.x) / 2f, (topLeft.y + bottomRight.y) / 2f)
//            }
//
//            // Определяем соотношение сторон и вычисляем масштабирование
//            val imageAspectRatio = imageSize.width / imageSize.height
//            val previewAspectRatio = previewViewSize.width.toFloat() / previewViewSize.height
//
//            val scale = if (imageAspectRatio > previewAspectRatio) {
//                previewViewSize.width / imageSize.width
//            } else {
//                previewViewSize.height / imageSize.height
//            }
//
//            val offsetX = if (imageAspectRatio > previewAspectRatio) {
//                0f
//            } else {
//                (previewViewSize.width - imageSize.width * scale) / 2f
//            }
//
//            val offsetY = if (imageAspectRatio > previewAspectRatio) {
//                (previewViewSize.height - imageSize.height * scale) / 2f
//            } else {
//                0f
//            }
//
//            // Масштабируем и смещаем точки QR-кода
//            val scaledTopLeft = Offset(topLeft.x * scale + offsetX, topLeft.y * scale + offsetY)
//            val scaledTopRight = Offset(topRight.x * scale + offsetX, topRight.y * scale + offsetY)
//            val scaledBottomRight = Offset(bottomRight.x * scale + offsetX, bottomRight.y * scale + offsetY)
//            val scaledBottomLeft = Offset(bottomLeft.x * scale + offsetX, bottomLeft.y * scale + offsetY)
//
//            translate(topLeft.x + offsetX, topLeft.y + offsetY){
//                drawRect(
//                    color = Color.Red,
//                    size = Size(10f, 10f)
//                )
//            }
//            // Рисуем рамку вокруг QR-кода
//            drawLine(
//                color = Color.Red,
//                start = scaledTopLeft,
//                end = scaledTopRight,
//                strokeWidth = 4f
//            )
//            drawLine(
//                color = Color.Red,
//                start = scaledTopRight,
//                end = scaledBottomRight,
//                strokeWidth = 4f
//            )
//            drawLine(
//                color = Color.Red,
//                start = scaledBottomRight,
//                end = scaledBottomLeft,
//                strokeWidth = 4f
//            )
//            drawLine(
//                color = Color.Red,
//                start = scaledBottomLeft,
//                end = scaledTopLeft,
//                strokeWidth = 4f
//            )
//        }
//    }
//}
//
//
//
//fun ConnectDevice(
//    code: String,
//    navController: NavHostController,
//){
//    if(code != "") {
//        _root_ide_package_.ru.ushell.app.old.api.websocket.qr.QRCodeConnect().connect()
//
//        val qrCodeDeliver =
//            _root_ide_package_.ru.ushell.app.old.api.websocket.qr.QRCodeDeliver(code)
//
//        qrCodeDeliver.connect(_root_ide_package_.ru.ushell.app.old.api.Config.webSocketAddressQR)
//
//        qrCodeDeliver.disconnect()
//        _root_ide_package_.ru.ushell.app.old.api.websocket.qr.QRCodeConnect().disconnect()
//
//        navController.navigate(_root_ide_package_.ru.ushell.app.old.ui.screens.drawer.models.device.RoutesDevice.ScreenDevice.route)
//    }
//}
