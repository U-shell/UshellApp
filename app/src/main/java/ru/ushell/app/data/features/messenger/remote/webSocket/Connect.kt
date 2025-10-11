package ru.ushell.app.data.features.messenger.remote.webSocket

import org.json.JSONObject
import ru.ushell.app.data.common.webSocket.TopicHandler
import ru.ushell.app.data.common.webSocket.WebSocketConnect
import ru.ushell.app.data.features.messenger.mappers.Message
import java.time.OffsetDateTime

class Connect(
    private val userChatId: String,
    override val url: String = "ws://192.168.1.78:8082/ws/websocket",
    private val onMessageReceived: (Message) -> Unit
) : WebSocketConnect {

    private var deliver: Deliver? = null

    override fun connect() {
        // Создаём Deliver только один раз
        if (deliver == null) {
            deliver = Deliver(userChatId)
        }

        // Подписываемся на очередь сообщений ДО подключения
        val topic = "/user/$userChatId/queue/messages"
        val topicHandler: TopicHandler = deliver!!.subscribe(topic)
        topicHandler.addListener { stompMessage ->
            try {
                // stompMessage.content — это JSON от сервера
                val json = JSONObject(stompMessage.content)
                val message = Message(
                    author = json.getString("senderId") == userChatId,
                    content = json.getString("message"),
                    timestamp = OffsetDateTime.now()
                )
//                MessageList.addMessages(message)
                onMessageReceived(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Подключаемся
        deliver!!.connect(url)
    }

    fun sendChatMessage(recipientId: String, message: String) {
        deliver?.sendChatMessage(recipientId, message)
    }

    override fun disconnect() {
        deliver?.disconnect()
        deliver = null
    }

    fun isConnected(): Boolean = deliver?.isConnect() == true
}