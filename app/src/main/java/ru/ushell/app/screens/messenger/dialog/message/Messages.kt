package ru.ushell.app.screens.messenger.dialog.message

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.ushell.app.data.features.messenger.mappers.Message
import ru.ushell.app.screens.messenger.MessageItem
import ru.ushell.app.screens.messenger.dialog.button.JumpToBottom
import java.time.OffsetDateTime

private val JumpToBottomThreshold = 56.dp

@Composable
fun MessageList(
    list: List<Message>,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    val jumpThreshold = with(LocalDensity.current) {
        JumpToBottomThreshold.toPx()
    }

    val jumpToBottomButtonEnabled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex != 0 ||
                    scrollState.firstVisibleItemScrollOffset > jumpThreshold
        }
    }

    Box(
        modifier = modifier
    ) {

        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (index in list.indices) {
                val content = list[index]

                val date = list.getOrNull(index)?.timestamp
                val preMessage = list.getOrNull(index - 1)?.timestamp

                if (date != null && preMessage != null) {
                    val dateMessage = date.dayOfWeek
                    val preMessageDay = preMessage.dayOfWeek

                    if (dateMessage != preMessageDay) {
                        item {
                            DayHeader("${preMessage.dayOfMonth} ${date.month.name}")
                        }
                    }
                }

                item {
                    MessageItem(
                        message = content,
                    )
                }
            }
        }

        JumpToBottom(
            enabled = jumpToBottomButtonEnabled,
            onClicked = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            },
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.BottomEnd)
        )
    }
}


@Composable
fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    HorizontalDivider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Preview
@Composable
fun MessageListPreview(){

    val mockMessages = listOf(
//        Message(
//            author = false,
//            content = "Привет! Как дела?\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",
//            timestamp = OffsetDateTime.now().minusDays(1)
//        ),
        Message(
            author = false,
            content = "Привет! Как дела?",
            timestamp = OffsetDateTime.now().minusDays(1)
        ),
        Message(
            author = false,
            content = "Привет! Как дела?",
            timestamp = OffsetDateTime.now().minusMinutes(3)
        ),
        Message(
            author = true,
            content = "Всё отлично! А у тебя?",
            timestamp = OffsetDateTime.now().minusMinutes(4)
        ),
        Message(
            author = false,
            content = "Тоже нормально 😊",
            timestamp = OffsetDateTime.now().minusMinutes(5)
        )
    ).reversed()

    MessageList(
        list = mockMessages,
        scrollState = rememberLazyListState()
    )
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun DayHeaderPreview(){
    DayHeader("1 NOVEMBER")
}