package ru.ushell.app.screens.profile.drawer.models.device.qrscanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.zxing.ResultPoint
import kotlinx.coroutines.launch


@Composable
fun CameraScreen(navController: NavHostController) {
    var code by remember { mutableStateOf("") }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var qrCodeCoordinates by remember { mutableStateOf<Array<ResultPoint>?>(null) }
    var previewViewSize by remember { mutableStateOf(IntSize.Zero) }
    var imageSize by remember { mutableStateOf(Size.Zero) }

    // Анимация рамки
    var targetFrame by remember { mutableStateOf(QRFrameState()) }
    var isQrDetected by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        var hasCameraPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            hasCameraPermission = granted
        }

        LaunchedEffect(Unit) {
            if (!hasCameraPermission) {
                launcher.launch(Manifest.permission.CAMERA)
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            if (hasCameraPermission) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
                            .onGloballyPositioned { layoutCoordinates ->
                                previewViewSize = layoutCoordinates.size
                            },
                        factory = { ctx ->

                            val previewView = PreviewView(ctx).apply {
                                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            }
                            previewView.scaleType = PreviewView.ScaleType.FIT_CENTER

                            val aspectRatio = AspectRatio.RATIO_16_9

                            val preview = Preview.Builder()
                                .setTargetAspectRatio(aspectRatio)
                                .build()

                            val imageAnalysis = ImageAnalysis.Builder()
                                .setTargetAspectRatio(aspectRatio) // ← ОБЯЗАТЕЛЬНО!
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            val selector = CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                .build()

                            preview.surfaceProvider = previewView.surfaceProvider

                            imageAnalysis.setAnalyzer(
                                ContextCompat.getMainExecutor(ctx),
                                QRCodeAnalyzer(
                                    onQrCodeScanned = { result ->
                                        result?.let {
                                            code = it
                                        }
                                    },
                                    onQrCodeCoordinate = { coordinates, size ->
                                        qrCodeCoordinates = coordinates
                                        imageSize = size
                                    }
                                )
                            )

                            try {
                                cameraProviderFuture.get().bindToLifecycle(lifecycleOwner, selector, preview, imageAnalysis)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            previewView
                        }
                    )

                    // Анимированная рамка
                    LaunchedEffect(qrCodeCoordinates, previewViewSize, imageSize) {
                        if (qrCodeCoordinates != null && previewViewSize != IntSize.Zero && imageSize != Size.Zero) {
                            val rect = calculateQRCodeRect(qrCodeCoordinates!!, imageSize, previewViewSize)
                            targetFrame = QRFrameState(rect.left, rect.top, rect.right, rect.bottom)
                            isQrDetected = true
                        } else {
                            // Сброс в центр
                            val center = Offset(previewViewSize.width / 2f, previewViewSize.height / 2f)
                            val half = 40f
                            targetFrame = QRFrameState(
                                left = center.x - half,
                                top = center.y - half,
                                right = center.x + half,
                                bottom = center.y + half
                            )
                            isQrDetected = false
                        }
                    }

                    val transition = updateTransition(targetFrame, label = "QRFrame")

                    val animatedLeft by transition.animateFloat(label = "left") { it.left }
                    val animatedTop by transition.animateFloat(label = "top") { it.top }
                    val animatedRight by transition.animateFloat(label = "right") { it.right }
                    val animatedBottom by transition.animateFloat(label = "bottom") { it.bottom }

                    QRCodeOverlay(
                        left = animatedLeft,
                        top = animatedTop,
                        right = animatedRight,
                        bottom = animatedBottom,
                        previewSize = previewViewSize
                    )
                }
            }
        }
    }

    // После сканирования — переход
    LaunchedEffect(code) {
        if (code.isNotEmpty()) {
            // Здесь должен быть ваш логический вызов подключения
            // ConnectDevice(code, navController)
//            navController.navigate("device_device") {
//                popUpTo("device_qr") { inclusive = true }
//            }
        }
    }
}

@Composable
fun QRCodeOverlay(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    previewSize: IntSize
) {
    if (previewSize == IntSize.Zero) return

    Canvas(modifier = Modifier.fillMaxSize()) {
        val rect = Rect(left = left, top = top, right = right, bottom = bottom)

        // Рамка
        drawRect(
            color = Color.Red,
            topLeft = Offset(rect.left, rect.top),
            size = Size(rect.width, rect.height),
            style = Stroke(width = 4f)
        )

        // Угловые линии (как раньше)
        val cornerLength = 20f
        val strokeWidth = 4f

        // Top-left
        drawLine(Color.Red, Offset(rect.left, rect.top), Offset(rect.left + cornerLength, rect.top), strokeWidth)
        drawLine(Color.Red, Offset(rect.left, rect.top), Offset(rect.left, rect.top + cornerLength), strokeWidth)

        // Top-right
        drawLine(Color.Red, Offset(rect.right, rect.top), Offset(rect.right - cornerLength, rect.top), strokeWidth)
        drawLine(Color.Red, Offset(rect.right, rect.top), Offset(rect.right, rect.top + cornerLength), strokeWidth)

        // Bottom-right
        drawLine(Color.Red, Offset(rect.right, rect.bottom), Offset(rect.right - cornerLength, rect.bottom), strokeWidth)
        drawLine(Color.Red, Offset(rect.right, rect.bottom), Offset(rect.right, rect.bottom - cornerLength), strokeWidth)

        // Bottom-left
        drawLine(Color.Red, Offset(rect.left, rect.bottom), Offset(rect.left + cornerLength, rect.bottom), strokeWidth)
        drawLine(Color.Red, Offset(rect.left, rect.bottom), Offset(rect.left, rect.bottom - cornerLength), strokeWidth)
    }
}

private fun calculateQRCodeRect(
    qrCodeCoordinates: Array<ResultPoint>,
    imageSize: Size,
    previewViewSize: IntSize
): Rect {
    if (qrCodeCoordinates.isEmpty()) return Rect(0f, 0f, 0f, 0f)

    val scaleX = previewViewSize.width.toFloat() / imageSize.width
    val scaleY = previewViewSize.height.toFloat() / imageSize.height
    val scale = minOf(scaleX, scaleY)

    val drawnWidth = imageSize.width * scale
    val drawnHeight = imageSize.height * scale
    val offsetX = (previewViewSize.width - drawnWidth) / 2f
    val offsetY = (previewViewSize.height - drawnHeight) / 2f

    val finalPoints = qrCodeCoordinates.map { point ->
        Offset(
            x = point.x * scale + offsetX,
            y = point.y * scale + offsetY
        )
    }

    val left = finalPoints.minOf { it.x }
    val right = finalPoints.maxOf { it.x }
    val top = finalPoints.minOf { it.y }
    val bottom = finalPoints.maxOf { it.y }

    // Создаём исходный bounding box
    val originalRect = Rect(left, top, right, bottom)

    // Увеличиваем рамку на 50% по диагонали → масштаб 1.5 относительно центра
    val centerX = (originalRect.left + originalRect.right) / 2f
    val centerY = (originalRect.top + originalRect.bottom) / 2f

    val width = originalRect.width
    val height = originalRect.height

    // Новые размеры: +50% ширины и высоты
    val newWidth = width * 1.5f
    val newHeight = height * 1.5f

    // Новые границы
    val newLeft = centerX - newWidth / 2f
    val newRight = centerX + newWidth / 2f
    val newTop = centerY - newHeight / 2f
    val newBottom = centerY + newHeight / 2f

    return Rect(newLeft, newTop, newRight, newBottom)
}

data class QRFrameState(
    val left: Float = 0f,
    val top: Float = 0f,
    val right: Float = 0f,
    val bottom: Float = 0f
)