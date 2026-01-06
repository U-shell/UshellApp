package ru.ushell.app.screens.setting.items.device.qrscanner

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.ui.geometry.Size
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer

class QRCodeAnalyzer(
    private val onQrCodeScanned: (result: String?) -> Unit,
    private val onQrCodeCoordinate: (coordinates: Array<ResultPoint>?, imageSize: Size) -> Unit,
) : ImageAnalysis.Analyzer {

    private var lastAnalysisTime = 0L
    private val minAnalysisIntervalMs = 300L

    companion object {
        private val SUPPORTED_IMAGE_FORMATS = listOf(ImageFormat.YUV_420_888)
    }

    override fun analyze(image: ImageProxy) {

        val now = System.currentTimeMillis()

        if (now - lastAnalysisTime < minAnalysisIntervalMs) {
            image.close()
            return
        }

        lastAnalysisTime = now

        if (image.format !in SUPPORTED_IMAGE_FORMATS) {
            onQrCodeScanned(null)
            onQrCodeCoordinate(null, Size(0f, 0f))
            image.close()
            return
        }

        try {
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(size = buffer.remaining()).also { buffer.get(it) }

            val dataWidth = image.width
            val dataHeight = image.height
            val sensorSize = Size(width = dataWidth.toFloat(), height = dataHeight.toFloat())

            val rotationDegrees = image.imageInfo.rotationDegrees

            val displayWidth = if (rotationDegrees == 90 || rotationDegrees == 270) dataHeight else dataWidth
            val displayHeight = if (rotationDegrees == 90 || rotationDegrees == 270) dataWidth else dataHeight

            val source = PlanarYUVLuminanceSource(
                bytes,
                dataWidth,
                dataHeight,
                0, 0,
                dataWidth,
                dataHeight,
                false
            )

            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val reader = MultiFormatReader().apply {
                setHints(mapOf(DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE)))
            }

            val result = reader.decode(binaryBitmap)

            onQrCodeScanned(result.text)

            onQrCodeCoordinate(
                adjustCoordinatesForRotation(
                    result.resultPoints,
                    rotationDegrees,
                    sensorSize
                ),
                Size(
                    width = displayWidth.toFloat(),
                    height = displayHeight.toFloat()
                )
            )

        } catch (e: Exception) {
            onQrCodeScanned(null)
        } finally {
            image.close()
        }
    }

    private fun adjustCoordinatesForRotation(
        points: Array<ResultPoint>,
        rotationDegrees: Int,
        sensorSize: Size
    ): Array<ResultPoint> {
        if (points.isEmpty()) return points

        return when (rotationDegrees) {
            0 -> points
            90 -> {
                val result = Array(points.size) { i ->
                    val p = points[i]
                    ResultPoint(sensorSize.height - p.y, p.x)
                }
                result
            }
            180 -> {
                val result = Array(points.size) { i ->
                    val p = points[i]
                    ResultPoint(sensorSize.width - p.x, sensorSize.height - p.y)
                }
                result
            }
            270 -> {
                val result = Array(points.size) { i ->
                    val p = points[i]
                    ResultPoint(p.y, sensorSize.width - p.x)
                }
                result
            }
            else -> points
        }
    }
}
