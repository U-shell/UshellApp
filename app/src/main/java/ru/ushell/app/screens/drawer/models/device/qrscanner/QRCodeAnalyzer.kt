//package ru.ushell.app.old.ui.screens.drawer.models.device.qrscanner
//
//import android.graphics.ImageFormat
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.ImageProxy
//import androidx.compose.ui.geometry.Size
//import com.google.zxing.*
//import com.google.zxing.common.HybridBinarizer
//import java.nio.ByteBuffer
//
//
//class QRCodeAnalyzer(
//    private val onQrCodeScanned: (result: String?) -> Unit,
//    private val onQrCodeCoordinate: (coordinates: Array<ResultPoint>?, imageSize: Size) -> Unit,
//) : ImageAnalysis.Analyzer {
//
//    companion object {
//        private val SUPPORTED_IMAGE_FORMATS = listOf(ImageFormat.YUV_420_888, ImageFormat.YUV_422_888, ImageFormat.YUV_444_888)
//    }
//
//    override fun analyze(image: ImageProxy) {
//        if (image.format in SUPPORTED_IMAGE_FORMATS) {
//            val bytes = image.planes.first().buffer.toByteArray()
//
//            val source = PlanarYUVLuminanceSource(
//                bytes,
//                image.width,
//                image.height,
//                0,
//                0,
//                image.width,
//                image.height,
//                false
//            )
//
//            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
//
//            try {
//                val result = MultiFormatReader().apply {
//                    setHints(
//                        mapOf(
//                            DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE)
//                        )
//                    )
//                }.decode(binaryBitmap)
//
//                onQrCodeScanned(result.text)
//
//                // Корректируем координаты с учетом ориентации камеры
//                val adjustedPoints = adjustCoordinatesForRotation(result.resultPoints, image.imageInfo.rotationDegrees, Size(image.width.toFloat(), image.height.toFloat()))
//                onQrCodeCoordinate(adjustedPoints, Size(image.width.toFloat(), image.height.toFloat()))
//
//            } catch (e: Exception) {
//                onQrCodeScanned(null)
//                onQrCodeCoordinate(null, Size(0f, 0f))
//            } finally {
//                image.close()
//            }
//        }
//    }
//
//    private fun adjustCoordinatesForRotation(
//        points: Array<ResultPoint>,
//        rotationDegrees: Int,
//        imageSize: Size
//    ): Array<ResultPoint> {
//        val adjustedPoints = mutableListOf<ResultPoint>()
//
//        for (point in points) {
//            var x = point.x
//            var y = point.y
//
//            when (rotationDegrees) {
//                90 -> {
//                    x = imageSize.height - point.y
//                    y = point.x
//                }
//                180 -> {
//                    x = imageSize.width - point.x
//                    y = imageSize.height - point.y
//                }
//                270 -> {
//                    x = point.y
//                    y = imageSize.width - point.x
//                }
//            }
//
//            adjustedPoints.add(ResultPoint(x, y))
//        }
//
//        return adjustedPoints.toTypedArray()
//    }
//
//    private fun ByteBuffer.toByteArray(): ByteArray {
//        rewind()
//        return ByteArray(remaining()).also { get(it) }
//    }
//}