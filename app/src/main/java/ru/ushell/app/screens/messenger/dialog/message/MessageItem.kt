package ru.ushell.app.screens.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import ru.ushell.app.data.features.messenger.mappers.Message
import java.time.OffsetDateTime

@Composable
fun MessageItem(
    message: Message,
) {
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .then(
                if (message.author)
                    Modifier.padding(
                        start = 50.dp,
                        end = 10.dp
                    )
                else
                    Modifier.padding(
                        start = 10.dp,
                        end = 50.dp,
                    )
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =
            if(message.author) {
                Arrangement.End
            } else {
                Arrangement.Start
            }
    ) {
        MessageItemContent(
            message = message,
        )
    }
}

@Composable
fun MessageItemContent(
    message: Message,
    modifier: Modifier = Modifier
) {
    val isUserMe = message.author

    val bubbleShape = if (isUserMe) {
        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 10.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 4.dp, bottomEnd = 10.dp)
    }

    Surface(
        shape = bubbleShape,
        contentColor = Color(0xFFD7D7D7),
        color = Color(0xFFD7D7D7),
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        PatternMessage(
            timeText = message.timestamp.formatAsTime(),
            text = message.content
        ){}

//        PatternMessage(
//            timeText = "22:53",
//            isPureText = false
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.qr_laptop),
//                contentDescription = null,
//                modifier = Modifier.size(200.dp)
//            )
//            Text("Подпись к фото", fontSize = 14.sp, color = Color.Black)
//        }
//        PatternMessage(
//            text = message.content,
//            timeText = message.timestamp.formatAsTime()
//        )
    }
}

@Composable
fun PatternMessage(
    timeText: String,
    modifier: Modifier = Modifier,
    isPureText: Boolean = true,
    text: String? = null, // только если isPureText = true
    content: @Composable () -> Unit,
) {
    if (isPureText && !text.isNullOrBlank()) {
        PureTextMessage(
            text = text,
            timeText = timeText,
            modifier = modifier
        )
        // === Режим 1: чисто текстовое сообщение (точная копия твоей первой версии) ===
    } else {
        // === Режим 2: всё остальное — всегда время внизу справа ===
        CustomContentMessage(
            timeText = timeText,
            modifier = modifier,
            content = content
        )
    }
}

// ───────────────────────────────────────────────
// Чисто текстовое сообщение — как в Telegram
@Composable
private fun PureTextMessage(
    text: String,
    timeText: String,
    modifier: Modifier = Modifier,
) {
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val inlineSpacingDp = 6.dp
    val density = LocalDensity.current


    SubcomposeLayout(
        modifier = modifier
            .padding(8.dp)
            .background(Color.Transparent)
    ) { constraints ->
        val inlineSpacingPx = with(density) { inlineSpacingDp.toPx().toInt() }

        // 1. Измеряем время
        val timePlaceable = subcompose("time") {
            Text(
                text = timeText,
                color = Color.Black,
                fontSize = 10.sp
            )
        }.first().measure(Constraints())

        // 2. Измеряем текст с минимальными ограничениями
        val textPlaceable = subcompose("text") {
            Text(
                text = text,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { layoutResult = it },
                maxLines = Int.MAX_VALUE,
            )
        }.first().measure(constraints.copy(minWidth = 0, minHeight = 0))

        // Определяем, можно ли разместить время рядом (в одну строку)
        val lineCount = layoutResult?.lineCount ?: 1
        val canFitInline = lineCount == 1 && layoutResult?.let { layout ->
            val textLineWidth = layout.getLineRight(0) - layout.getLineLeft(0)
            (textLineWidth + inlineSpacingPx + timePlaceable.width) <= constraints.maxWidth
        } == true


        // Ширина контейнера — минимум ширины текста, максимум — ограничение
        val layoutWidth = if (canFitInline) {
            minOf(
                (layoutResult?.let { it.getLineRight(0) - it.getLineLeft(0) }?.toInt()
                    ?: textPlaceable.width) + inlineSpacingPx + timePlaceable.width,
                constraints.maxWidth
            )
        } else {
            textPlaceable.width.coerceAtMost(constraints.maxWidth)
        }.coerceAtLeast(constraints.minWidth)

        // Пересчитываем текст, если нужно подрезать по ширине
        var finalTextPlaceable = textPlaceable

        if (!canFitInline && layoutWidth != textPlaceable.width) {
            finalTextPlaceable = subcompose("text_remeasured") {
                Text(
                    text = text,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { layoutResult = it },
                    maxLines = Int.MAX_VALUE,
                )
            }.first().measure(
                constraints.copy(minWidth = layoutWidth, maxWidth = layoutWidth)
            )
        }

        // Высота: если в одну строку — высота текста, иначе — текст + время
        val layoutHeight = if (canFitInline) {
            finalTextPlaceable.height
        } else {
            finalTextPlaceable.height + timePlaceable.height
        }

        layout(layoutWidth, layoutHeight) {
            // Размещаем текст
            finalTextPlaceable.place(0, 0)

            // Размещаем время
            val timeX = if (canFitInline) {
                // В одну строку — справа от текста
                finalTextPlaceable.width + inlineSpacingPx
            } else {
                // Многострочно — внизу справа
                layoutWidth - timePlaceable.width
            }

            val timeY = if (canFitInline) {
                // На одном уровне с текстом (выравниваем по baseline)
                val textBaseline = finalTextPlaceable[LastBaseline]
                val timeBaseline = timePlaceable[LastBaseline]

                if (textBaseline != AlignmentLine.Unspecified && timeBaseline != AlignmentLine.Unspecified) {
                    finalTextPlaceable.height - (textBaseline / 2)
                } else {
                    (finalTextPlaceable.height - timePlaceable.height) / 2
                }
            } else {
                // Под текстом
                finalTextPlaceable.height
            }

            timePlaceable.place(timeX, timeY)
        }
    }
}


// ───────────────────────────────────────────────
// Любое другое содержимое — всегда время внизу справа
@Composable
private fun CustomContentMessage(
    timeText: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(5.dp)
    ) {
        // Контент занимает всю ширину по мере необходимости
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            content()
        }

        // Время — внизу справа
        Text(
            text = timeText,
            fontSize = 10.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(start = 6.dp, top = 2.dp)
        )
    }
}
fun OffsetDateTime.formatAsTime(): String {
    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}

@Preview
@Composable
fun MessageItemAsuthorPreview(){
    MessageItem(
        message = Message(
            author = true,
            content = "Привет! Как дела?",
            timestamp = OffsetDateTime.now()
        ),
    )
}

@Preview
@Composable
fun MessageItemAuthorPreview(){
    MessageItem(
        message = Message(
            author = true,
            content = "Привет! Как дела?",
            timestamp = OffsetDateTime.now()
        ),
    )
}

@Preview
@Composable
fun MessageItemLongMessage1Preview(){
    MessageItem(
        message = Message(
            author = true,
            content = "Это очень длинное сообщение, которое покажет",
            timestamp = OffsetDateTime.now()
        ),
    )
}
