package ru.ushell.app.screens.messenger.dialog.components.button.emoji

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ushell.app.screens.drawer.emojiList

@Composable
fun EmojiSelector(
    onTextAdded: (String) -> Unit,
) {
    EmojiSelectorContext(onTextAdded)
}


@Composable
fun EmojiSelectorContext(
    onTextAdded: (String) -> Unit,

    ) {
    var selected by remember { mutableStateOf(EmojiStickerSelector.EMOJI) }

    Column(
        modifier = Modifier
            .focusTarget()
    ) {
        EmojiTable(
            onEmojiSelected = onTextAdded
        )
    }
}

@Composable
fun EmojiTable(
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    categoryIndex: Int = 0
) {
    val emojiCategory = emojiList.getOrNull(categoryIndex)
        ?.takeIf { it.isNotEmpty() }
        ?: emptyArray()

    LazyVerticalGrid(
        columns = GridCells.Fixed(EMOJI_COLUMNS),
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(emojiCategory.size) { index ->
            val emoji = emojiCategory[index]
            EmojiItem(
                emoji = emoji,
                onClick = { onEmojiSelected(emoji) }
            )
        }
    }
}

@Composable
private fun EmojiItem(
    emoji: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

private const val EMOJI_COLUMNS = 10

enum class EmojiStickerSelector {
    EMOJI,
    STICKER
}

@Preview
@Composable
fun EmojiSelectorPreview() {
    EmojiSelector(onTextAdded = {})
}
