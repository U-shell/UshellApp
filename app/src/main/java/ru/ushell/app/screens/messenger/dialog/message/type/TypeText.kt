package ru.ushell.app.screens.messenger.dialog.message.type

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TextMessage(
    timeMessage: String,
    message: String = "",
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null,
) {
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val inlineSpacingDp = 6.dp
    val density = LocalDensity.current

    SubcomposeLayout(
        modifier = modifier
            .background(Color.Transparent)
            .padding(bottom = 5.dp)
    ) { constraints ->
        val inlineSpacingPx = with(density) { inlineSpacingDp.toPx().toInt() }

        val contextPlaceable = if (content != null) {
            subcompose("context") { content() }
                .first()
                .measure(constraints.copy(minWidth = 0))
        } else null

        val timePlaceable = subcompose("time") {
            Text(
                text = timeMessage,
                color = Color.Black,
                fontSize = 10.sp,
                modifier = Modifier
                    .then(
                        if (contextPlaceable != null) {
                            Modifier
                                .padding(end = 5.dp, bottom = 8.dp)
                        } else {
                            Modifier.padding(end = 5.dp)
                        }
                    )
            )
        }.first().measure(Constraints())

        val textPlaceable = if (message.isNotEmpty()) {
            subcompose("text") {
                Text(
                    text = message,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { layoutResult = it },
                    maxLines = Int.MAX_VALUE,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }.first().measure(constraints.copy(minWidth = 0))
        } else null


        // === Определяем, может ли текст + время поместиться в одну строку ===
        val textLayoutResult = layoutResult
        val textLineCount = textLayoutResult?.lineCount ?: 1
        val textWidth = textLayoutResult?.let { it.getLineRight(0) - it.getLineLeft(0) }?.toInt()
            ?: textPlaceable?.width ?: 0

        val canTextFitInline = textLineCount == 1 && (textWidth + inlineSpacingPx + timePlaceable.width) <= constraints.maxWidth

        // === Основная ширина: максимум из текста, контекста и (текст+время) ===
        val candidateWidths = mutableListOf(constraints.minWidth)

        // Ширина текста + время (если в одну строку)
        if (canTextFitInline) {
            candidateWidths.add(textWidth + inlineSpacingPx + timePlaceable.width)
        } else {
            candidateWidths.add(textWidth)
        }

        // Ширина контекста (например, изображения)
        contextPlaceable?.let { candidateWidths.add(it.width) }

        val layoutWidth = candidateWidths.maxOrNull()?.coerceAtMost(constraints.maxWidth)
            ?: constraints.minWidth

        // === Пересчитываем текст с учётом layoutWidth (если нужно) ===
        var finalTextPlaceable = textPlaceable
        if (textPlaceable != null && layoutWidth != textPlaceable.width) {
            finalTextPlaceable = subcompose("text_remeasured") {
                Text(
                    text = message,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = Int.MAX_VALUE,
                    modifier = Modifier
                        .padding(8.dp)

                )
            }.first().measure(
                constraints.copy(minWidth = layoutWidth, maxWidth = layoutWidth)
            )
        }

        // === Высота ===
        val contextHeight = contextPlaceable?.height ?: 0
        val textHeight = finalTextPlaceable?.height ?: 0
        val layoutHeight = if (canTextFitInline) {
            contextHeight + textHeight // время в той же строке, что и текст
        } else {
            contextHeight + textHeight + timePlaceable.height
        }


        layout(layoutWidth.coerceAtLeast(1), layoutHeight.coerceAtLeast(1)) {
            var yOffset = 0
            // 1. Контекст (изображение)
            contextPlaceable?.place(0, yOffset)
            yOffset += contextHeight

            // 2. Текст
            finalTextPlaceable?.place(0, yOffset)

            // 3. Время
            val timeX = if (canTextFitInline) {
                (finalTextPlaceable?.width ?: 0) - timePlaceable.width
            } else {
                layoutWidth - timePlaceable.width
            }

            val timeY = if (canTextFitInline) {
                val textBaseline = finalTextPlaceable?.get(LastBaseline) ?: AlignmentLine.Unspecified
                val timeBaseline = timePlaceable[LastBaseline]
                if (textBaseline != AlignmentLine.Unspecified && timeBaseline != AlignmentLine.Unspecified) {
                    yOffset + (finalTextPlaceable!!.height - textBaseline / 2)
                } else {
                    yOffset + (finalTextPlaceable?.height ?: (0 - timePlaceable.height)) / 2
                }
            } else {
                // Под текстом
                yOffset + (finalTextPlaceable?.height ?: 0)
            }
            if (textPlaceable == null && contextPlaceable != null){
                timePlaceable.place(contextPlaceable.width - timePlaceable.width,timeY)

            }else{
                timePlaceable.place(timeX, timeY)
            }
        }
    }
}

@Preview
@Composable
fun TextMessagePreview(){
    TextMessage(
        timeMessage= "12:30",
        message = "Привет!"
    )
}