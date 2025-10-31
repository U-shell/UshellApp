package ru.ushell.app.screens.profile.drawer.models.device.qrscanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.google.zxing.ResultPoint
import ru.ushell.app.screens.profile.drawer.models.device.RoutesDevice
import ru.ushell.app.screens.profile.drawer.models.device.view.DeviceVewModel

@Composable
fun CameraScreen(
    navController: NavHostController,
    viewModel: DeviceVewModel = hiltViewModel()
) {
    var code by remember { mutableStateOf("") }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var qrCodeCoordinates by remember { mutableStateOf<Array<ResultPoint>?>(null) }
    var previewViewSize by remember { mutableStateOf(IntSize.Zero) }
    var imageSize by remember { mutableStateOf(Size.Zero) }
    var targetFrame by remember { mutableStateOf(QRFrameState()) }

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

    LaunchedEffect(code) {

        if (code.isNotEmpty()) {
            //TODO: дождаться ответа и только тогда перехожить
            viewModel.sendMessage(code)
            navController.navigate(RoutesDevice.ScreenDevice) {
                popUpTo(RoutesDevice.ScreenQR) { inclusive = true }
            }
        }
    }

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
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }

                    val aspectRatioStrategy = AspectRatioStrategy(
                        AspectRatio.RATIO_4_3,
                        AspectRatioStrategy.FALLBACK_RULE_NONE
                    )

                    val resolutionSelector = ResolutionSelector.Builder()
                        .setAspectRatioStrategy(aspectRatioStrategy)
                        .build()

                    val preview = Preview.Builder()
                        .setResolutionSelector(resolutionSelector)
                        .build()

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setResolutionSelector(resolutionSelector)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    val cameraSelector = CameraSelector.Builder()
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
                        cameraProviderFuture.get()
                            .bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    previewView
                }
            )

            LaunchedEffect(qrCodeCoordinates, previewViewSize, imageSize) {

                if (qrCodeCoordinates != null && previewViewSize != IntSize.Zero && imageSize != Size.Zero) {
                    val rect = calculateQRCodeRect(qrCodeCoordinates!!, imageSize, previewViewSize)

                    targetFrame = QRFrameState(
                        left = rect.left,
                        top = rect.top,
                        right = rect.right,
                        bottom = rect.bottom
                    )

                } else {
                    val center = Offset(previewViewSize.width / 2f, previewViewSize.height / 4f)
                    val half = 300f

                    targetFrame = QRFrameState(
                        left = center.x - half,
                        top = center.y - half,
                        right = center.x + half,
                        bottom = center.y + half
                    )
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

@Composable
fun QRCodeOverlay(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    previewSize: IntSize
) {
    if (previewSize == IntSize.Zero || left >= right || top >= bottom) return

    val radiusPx = with(LocalDensity.current) { 10.dp.toPx() }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val overlayColor = Color(0x80000000)

        // Сверху
        drawRect(color = overlayColor, size = Size(canvasWidth, top))

        // Снизу
        drawRect(
            color = overlayColor,
            topLeft = Offset(0f, bottom),
            size = Size(canvasWidth, canvasHeight - bottom)
        )
        // Слева
        drawRect(
            color = overlayColor,
            topLeft = Offset(0f, top),
            size = Size(left, bottom - top)
        )
        // Справа
        drawRect(
            color = overlayColor,
            topLeft = Offset(right, top),
            size = Size(canvasWidth - right, bottom - top)
        )

        // Контур области сканирования
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(left, top),
            size = Size(right - left, bottom - top),
            style = Stroke(width = 20f),
            cornerRadius = CornerRadius(radiusPx, radiusPx)
        )
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

    val scale = maxOf(scaleX, scaleY)

    val drawnWidth = imageSize.width * scale
    val drawnHeight = imageSize.height * scale

    val offsetX = if (drawnWidth > previewViewSize.width) {
        (previewViewSize.width - drawnWidth) * 0.5f
    } else {
        0f
    }

    val offsetY = if (drawnHeight > previewViewSize.height) {
        (previewViewSize.height - drawnHeight) * 0.5f
    } else {
        0f
    }

    var minX = Float.POSITIVE_INFINITY
    var maxX = Float.NEGATIVE_INFINITY
    var minY = Float.POSITIVE_INFINITY
    var maxY = Float.NEGATIVE_INFINITY

    for (point in qrCodeCoordinates) {
        val x = point.x * scale + offsetX
        val y = point.y * scale + offsetY

        if (x < minX) minX = x
        if (x > maxX) maxX = x
        if (y < minY) minY = y
        if (y > maxY) maxY = y
    }


    val centerX = (minX + maxX) * 0.5f
    val centerY = (minY + maxY) * 0.5f
    val width = maxX - minX
    val height = maxY - minY

    val newHalfWidth = width * 0.75f
    val newHalfHeight = height * 0.75f

    return Rect(
        left = centerX - newHalfWidth,
        top = centerY - newHalfHeight,
        right = centerX + newHalfWidth,
        bottom = centerY + newHalfHeight
    )
}

data class QRFrameState(
    val left: Float = 0f,
    val top: Float = 0f,
    val right: Float = 0f,
    val bottom: Float = 0f
)