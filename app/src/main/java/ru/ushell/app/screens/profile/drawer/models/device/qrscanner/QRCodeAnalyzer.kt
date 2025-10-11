package ru.ushell.app.screens.profile.drawer.models.device.qrscanner
import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.ui.geometry.Size
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

class QRCodeAnalyzer(
    private val onQrCodeScanned: (result: String?) -> Unit,
    private val onQrCodeCoordinate: (coordinates: Array<ResultPoint>?, imageSize: Size) -> Unit,
) : ImageAnalysis.Analyzer {

    companion object {
        private val SUPPORTED_IMAGE_FORMATS = listOf(ImageFormat.YUV_420_888)
    }

    override fun analyze(image: ImageProxy) {
        if (image.format !in SUPPORTED_IMAGE_FORMATS) {
            onQrCodeScanned(null)
            onQrCodeCoordinate(null, Size(0f, 0f))
            image.close()
            return
        }

        try {
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining()).also { buffer.get(it) }

            // Определяем размер изображения ДО поворота
            val sensorWidth = image.width
            val sensorHeight = image.height
            val rotation = image.imageInfo.rotationDegrees

            // Размер изображения ПОСЛЕ поворота (тот, который видит пользователь)
            val displayWidth = if (rotation == 90 || rotation == 270) sensorHeight else sensorWidth
            val displayHeight = if (rotation == 90 || rotation == 270) sensorWidth else sensorHeight

            val source = PlanarYUVLuminanceSource(
                bytes,
                sensorWidth,
                sensorHeight,
                0, 0,
                sensorWidth,
                sensorHeight,
                false
            )

            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val reader = MultiFormatReader().apply {
                setHints(mapOf(DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE)))
            }

            val result = reader.decode(binaryBitmap)

            onQrCodeScanned(result.text)

            // Передаём ОРИГИНАЛЬНЫЙ размер сенсора в adjustCoordinatesForRotation
            val adjustedPoints = adjustCoordinatesForRotation(
                result.resultPoints,
                rotation,
                Size(sensorWidth.toFloat(), sensorHeight.toFloat())
            )

            // Но для масштабирования в UI мы используем РАЗМЕР ПОСЛЕ ПОВОРОТА
            onQrCodeCoordinate(adjustedPoints, Size(displayWidth.toFloat(), displayHeight.toFloat()))

        } catch (e: Exception) {
            onQrCodeScanned(null)
            onQrCodeCoordinate(null, Size(0f, 0f))
        } finally {
            image.close()
        }
    }

    private fun adjustCoordinatesForRotation(
        points: Array<ResultPoint>,
        rotationDegrees: Int,
        sensorSize: Size // Размер сенсора (до поворота)
    ): Array<ResultPoint> {
        return when (rotationDegrees) {
            0 -> points
            90 -> {
                // (x, y) в сенсоре → (sensorHeight - y, x) в отображаемом изображении
                points.map { ResultPoint(sensorSize.height - it.y, it.x) }.toTypedArray()
            }
            180 -> {
                points.map { ResultPoint(sensorSize.width - it.x, sensorSize.height - it.y) }.toTypedArray()
            }
            270 -> {
                // (x, y) → (y, sensorWidth - x)
                points.map { ResultPoint(it.y, sensorSize.width - it.x) }.toTypedArray()
            }
            else -> points
        }
    }
}