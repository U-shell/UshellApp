package ru.ushell.app.data.features.messenger.remote.webSocket

import org.json.JSONObject
import ru.ushell.app.data.common.webSocket.TopicHandler
import ru.ushell.app.data.common.webSocket.WebSocketConnect
import ru.ushell.app.data.features.messenger.dto.MessageType
import ru.ushell.app.data.features.messenger.mappers.Message
import java.time.OffsetDateTime

class Connect(
    private val userChatId: String,
    private val onMessageReceived: (Message) -> Unit
) : WebSocketConnect {

    private var deliver: Deliver? = null

    override fun connect() {

        if (deliver == null) {
            deliver = Deliver(userChatId)
        }

        // Подписываемся на очередь сообщений ДО подключения
        val topic = "/user/$userChatId/queue/messages"
        val topicHandler: TopicHandler = deliver!!.subscribe(topic)
        topicHandler.addListener { stompMessage ->
            try {
                // stompMessage.content — это JSON от сервера
                // TODO переделать
                val json = JSONObject(stompMessage.content)
                val message = Message(
                    author = json.getString("senderId") == userChatId,
                    message = json.getString("message"),
                    type = MessageType.valueOf(json.getString("type")),
                    fileName = json.getString("fileName"),
                    timestamp = OffsetDateTime.now()
                )
//                MessageList.addMessages(message)
                onMessageReceived(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Подключаемся
        deliver!!.connect(url = "/messenger")
    }

    override fun disconnect() {
        deliver?.disconnect()
        deliver = null
    }

    fun sendChatMessage(recipientId: String, message: Message) {
        deliver?.sendMessage(recipientId, message)
    }

    fun isConnected(): Boolean = deliver?.isConnect() == true
}