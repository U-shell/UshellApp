package ru.ushell.app.screens.profile.diagram

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ushell.app.ui.theme.DiagramBackgroundColor
import ru.ushell.app.ui.theme.DiagramBaseColor
import ru.ushell.app.ui.theme.ListColorRing


@Composable
fun ProgressItem(
    progress: Progress,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp)),
        color = DiagramBackgroundColor
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .wrapContentWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircleProgressCustom(
                allSteps = progress.allClasses,
                finishedSteps = progress.finishedClasses,
                modifier = Modifier
                    .padding(4.dp)
            )

            Text(
                text = progress.name,
                fontSize = 10.sp,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                ),
                lineHeight = 12.sp, // лучше 12.sp для 10.sp шрифта
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 4.dp)
            )
        }
    }
}

@Composable
internal fun CircleProgressCustom(
    allSteps: Int,
    finishedSteps: Int,
    textFontSize: Dp = 27.dp,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {

        val canvasSize = 200
        val sizeStroke = 10.dp // ширина диаграмы
        val size = Size(width = canvasSize.toFloat(), height = canvasSize.toFloat())
        val canvasSizeDp = with(receiver = LocalDensity.current) { canvasSize.toDp() }

        val sweepAngle = finishedSteps * 360f / allSteps
        val sliceWidthPx = with(LocalDensity.current) { sizeStroke.toPx() }

        val brush =  Brush.linearGradient(
            colors = ListColorRing,
            start = Offset(size.width, 0f),
            end = Offset(0f, size.height)
        )

        val present = ((finishedSteps.toDouble() / allSteps) * 100).toInt()
        val text = "${present}%"
        val textFontSize = with(LocalDensity.current) { textFontSize.toPx() }
        val textPaint =
            Paint().apply {
                color = Color.Black.toArgb()
                textSize = textFontSize
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT_BOLD
            }


        Canvas(
            modifier = Modifier.size(canvasSizeDp)
        ) {

            drawArc(
                color = DiagramBaseColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                size = size,
                style = Stroke(width = sliceWidthPx)
            )

            if (sweepAngle > 0f) {
                drawArc(
                    brush = brush,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    size = size,
                    style = Stroke(width = sliceWidthPx, cap = StrokeCap.Butt)
                )
            }

            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    text,
                    (canvasSize / 2).toFloat(),
                    (canvasSize - 75).toFloat(),
                    textPaint
                )
            }
        }
    }
}


@Preview
@Composable
fun ProgressItemPreview() {
    ProgressItem(
        progress = Progress(
            name = "Title name",
            allClasses = 20,
            finishedClasses = 5,
            colorMain = Color(0xFF6650a4),
            progressColor = Color(0xFF6650a4),
        ),
        modifier = Modifier
            .padding(horizontal = 5.dp)
    )
}

@Preview
@Composable
fun CircleProgressCustomPreview(){
    CircleProgressCustom(
        allSteps = 20,
        finishedSteps = 5,
        modifier = Modifier
            .padding(4.dp)
    )
}
