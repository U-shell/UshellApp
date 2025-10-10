package ru.ushell.app.data.features.messanger.remote.webSocket

import org.json.JSONObject
import ru.ushell.app.data.common.webSocket.TopicHandler
import ru.ushell.app.data.common.webSocket.WebSocket
import ru.ushell.app.data.common.webSocket.stomp.StompMessage
import ru.ushell.app.data.common.webSocket.stomp.StompMessageListener

class Connect: WebSocket {

    private var deliver: Deliver? = null

    override fun connect() {
        deliver = Deliver()

        val topicHandler: TopicHandler = deliver?.subscribe(
            "/user/$/queue/messages"
        ) ?: throw IllegalArgumentException("TopicHandler is null Error in messenger")

        topicHandler.addListener { message ->
            try {
                val jsonObject = JSONObject(message.content)
                val msg = jsonObject.getString("message")
//                val isAuth = jsonObject.getString("senderId") == User.keyIdUserChat

//                MessageList.addMessages(
//                    Message(
//                        auth = isAuth,
//                        message = msg,
//                        time = OffsetDateTime.now()
//                    )
//                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        deliver!!.connect("Config.webSocketAddress")
    }


    override fun disconnect() {
        deliver?.disconnect()
        deliver = null
    }

}