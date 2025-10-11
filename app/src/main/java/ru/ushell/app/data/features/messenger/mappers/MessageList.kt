package ru.ushell.app.data.features.messenger.mappers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList

class MessageList() {
    val channelName: String?
        get() = Companion.channelName

    var recipientId: String?
        get() = Companion.recipientId
        set(recipientId) {
            Companion.recipientId = recipientId
        }


    val messageList: List<Message>
        get() = _messages


    fun addMessage(message: Message) {
        _messages.add(0, message)
    }


    companion object {
        private var channelName: String? = null
        private var recipientId: String? = null
        private var _messages: MutableList<Message> = mutableStateListOf()

        @JvmStatic
        fun setChannelName(channelName: String?) {
            Companion.channelName = channelName
        }

        @JvmStatic
        fun setMessageList(messageList: List<Message>) {
            _messages = messageList.toMutableStateList()
        }

        @JvmStatic
        fun addMessages(message: Message) {
            if (_messages.none { it.timestamp == message.timestamp }) {
                _messages.add(0, message)
            }
        }
    }
}
