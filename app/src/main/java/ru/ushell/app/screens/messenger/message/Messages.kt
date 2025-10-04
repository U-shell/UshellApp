//package ru.ushell.app.old.ui.screens.chatScreen.message
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.RowScope
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyListState
//import androidx.compose.material3.Divider
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.launch
//import ru.ushell.app.models.modelChat.message.Message
//import ru.ushell.app.ui.screens.chatScreen.dialog.JumpToBottom
//import java.util.Locale
//
//private val JumpToBottomThreshold = 56.dp
//
//@Composable
//fun Messages(
//    messages: List<ru.ushell.app.old.models.modelChat.message.Message>,
//    scrollState: LazyListState,
//    modifier: Modifier = Modifier
//) {
//    val scope = rememberCoroutineScope()
//    Box(modifier = modifier) {
//
//        LazyColumn(
//            reverseLayout = true,
//            state = scrollState,
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            for (index in messages.indices) {
//                val prevAuthor = messages.getOrNull(index - 1)?.author
//                val nextAuthor = messages.getOrNull(index + 1)?.author
//                val content = messages[index]
//                val isFirstMessageByAuthor = prevAuthor != content.author
//                val isLastMessageByAuthor = nextAuthor != content.author
//
//                val date = messages.getOrNull(index)?.timestamp
//                val preMessage = messages.getOrNull(index - 1)?.timestamp
//
//                if (date != null && preMessage != null) {
//                    val dateMessage = date.dayOfWeek
//                    val preMessageDay = preMessage.dayOfWeek
//
//                    // Now you can safely compare the days of the week
//                    if (dateMessage != preMessageDay) {
//                        item {
//                            DayHeader("${preMessage.dayOfMonth} ${date.month.name}")
//                        }
//                    }
//                }
//
//
//                item {
//                    Message(
//                        msg = content,
//                        isFirstMessageByAuthor = isFirstMessageByAuthor,
//                        isLastMessageByAuthor = isLastMessageByAuthor
//                    )
//                }
//            }
//        }
//
//        val jumpThreshold = with(LocalDensity.current) {
//            JumpToBottomThreshold.toPx()
//        }
//
//
//        val jumpToBottomButtonEnabled by remember {
//            derivedStateOf {
//                scrollState.firstVisibleItemIndex != 0 ||
//                        scrollState.firstVisibleItemScrollOffset > jumpThreshold
//            }
//        }
//
//        _root_ide_package_.ru.ushell.app.old.ui.screens.chatScreen.dialog.JumpToBottom(
//            enabled = jumpToBottomButtonEnabled,
//            onClicked = {
//                scope.launch {
//                    scrollState.animateScrollToItem(0)
//                }
//            },
//            modifier = Modifier.align(Alignment.BottomEnd)
//        )
//    }
//}
//
//
//@Composable
//fun DayHeader(dayString: String) {
//    Row(
//        modifier = Modifier
//            .padding(vertical = 8.dp, horizontal = 16.dp)
//            .height(16.dp)
//    ) {
//        DayHeaderLine()
//        Text(
//            text = dayString,
//            modifier = Modifier.padding(horizontal = 16.dp),
//            style = MaterialTheme.typography.labelSmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant
//        )
//        DayHeaderLine()
//    }
//}
//
//@Composable
//private fun RowScope.DayHeaderLine() {
//    HorizontalDivider(
//        modifier = Modifier
//            .weight(1f)
//            .align(Alignment.CenterVertically),
//        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
//    )
//}