package ru.ushell.app.ui.screens.chatScreen.dialog

import ru.ushell.app.models.modelChat.message.Message
import ru.ushell.app.ui.screens.chatScreen.dialog.EMOJIS.EMOJI_CLOUDS
import ru.ushell.app.ui.screens.chatScreen.dialog.EMOJIS.EMOJI_FLAMINGO
import ru.ushell.app.ui.screens.chatScreen.dialog.EMOJIS.EMOJI_MELTING
import ru.ushell.app.ui.screens.chatScreen.dialog.EMOJIS.EMOJI_PINK_HEART
import ru.ushell.app.ui.screens.chatScreen.dialog.EMOJIS.EMOJI_POINTS

//private val initialMessages = listOf(
//    Message(
//        author = true,
//        content = "Check it out!",
//        timestamp = "8:07 PM"
//    ),
//    Message(
//        author = true,
//         content = "Thank you!$EMOJI_PINK_HEART",
//        timestamp ="8:06 PM"
//    ),
//    Message(
//        author = false,
//        content = "You can use all the same stuff",
//        timestamp ="8:05 PM"
//    ),
//    Message(
//        author = true,
//        content = "@aliconors Take a look at the `Flow.collectAsStateWithLifecycle()` APIs",
//        timestamp = "8:05 PM"
//    ),
//    Message(
//        author = false,
//        content = "Compose newbie as well $EMOJI_FLAMINGO, have you looked at the JetNews sample? " +
//                "Most blog posts end up out of date pretty fast but this sample is always up to " +
//                "date and deals with async data loading (it's faked but the same idea " +
//                "applies) $EMOJI_POINTS https://goo.gle/jetnews",
//        timestamp ="8:04 PM"
//    ),
//    Message(
//        author = true,
//        content = "Compose newbie: I’ve scourged the internet for tutorials about async data " +
//                "loading but haven’t found any good ones $EMOJI_MELTING $EMOJI_CLOUDS. " +
//                "What’s the recommended way to load async data and emit composable widgets?",
//        timestamp ="8:03 PM"
//    )
//)

//val exampleUiState = MessageList(
//    initialMessages = initialMessages,
//    chatName = "#composers",
//    channelMembers = 42
//)



object EMOJIS {
    // EMOJI 15
    const val EMOJI_PINK_HEART = "\uD83E\uDE77"

    // EMOJI 14 🫠
    const val EMOJI_MELTING = "\uD83E\uDEE0"

    // ANDROID 13.1 😶‍🌫️
    const val EMOJI_CLOUDS = "\uD83D\uDE36\u200D\uD83C\uDF2B️"

    // ANDROID 12.0 🦩
    const val EMOJI_FLAMINGO = "\uD83E\uDDA9"

    // ANDROID 12.0  👉
    const val EMOJI_POINTS = " \uD83D\uDC49"
}