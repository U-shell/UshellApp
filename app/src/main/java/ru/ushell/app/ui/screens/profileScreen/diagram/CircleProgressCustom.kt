package ru.ushell.app.ui.screens.profileScreen.diagram

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.ushell.app.ui.theme.DiagramBaseColor
import ru.ushell.app.ui.theme.ListColorRing

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun CircleProgressCustom(
    modifier: Modifier = Modifier,
    allSteps: Int,
    finishedSteps: Int,
    textFontSize: Dp = 27.dp
) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
//        val canvasSize = min(a = constraints.maxWidth, b = constraints.maxHeight)
        val canvasSize = 200
        val sizeStroke = 10.dp // ширина диаграмы
        val size = Size(width = canvasSize.toFloat(), height = canvasSize.toFloat())
        val canvasSizeDp = with(receiver = LocalDensity.current) { canvasSize.toDp() }
        val progressArch = finishedSteps * 360f / allSteps
        val sliceWidthPx = with(LocalDensity.current) { sizeStroke.toPx() }
        val textFontSize = with(LocalDensity.current) { textFontSize.toPx() }
        val present = ((finishedSteps.toDouble() / allSteps) * 100).toInt()

        Canvas(modifier = Modifier.size(canvasSizeDp)) {
            drawArc(
                color = DiagramBaseColor,
                startAngle = 0f,
                sweepAngle = 360f,//угол поворота
                useCenter = false,
                size = size,
                style = Stroke(width = sliceWidthPx)
            )
            val brush =  Brush.linearGradient(
                colors = ListColorRing,
                start = Offset(size.width, 0f), // Начало градиента (справа)
                end = Offset(0f, size.height)
            )

            drawArc(
                brush = brush,
                startAngle = 270f,//угол поворота
                sweepAngle = progressArch,
                useCenter = false,
                size = size,
                style = Stroke(width = sliceWidthPx, cap = StrokeCap.Butt)
            )

            val textPaint =
                Paint().apply {
                    color = Color.Black.toArgb()
                    textSize = textFontSize
                    textAlign = Paint.Align.CENTER
                    typeface = Typeface.DEFAULT_BOLD
                }

            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    "${present}%",
                    (canvasSize / 2).toFloat(),
                    (canvasSize - 75).toFloat(),
                    textPaint
                )
            }
        }
    }
}
