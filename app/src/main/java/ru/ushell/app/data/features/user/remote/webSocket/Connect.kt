package ru.ushell.app.data.features.user.remote.webSocket

import org.json.JSONObject
import ru.ushell.app.data.common.webSocket.TopicHandler
import ru.ushell.app.data.common.webSocket.WebSocket
import ru.ushell.app.data.features.messanger.remote.webSocket.Deliver

class Connect: WebSocket {

    private var deliver: Deliver? = null

    override fun connect() {
        deliver = Deliver()
        val topicHandler: TopicHandler = deliver?.subscribe(
            "/topic/loginListener/"
        ) ?: throw IllegalArgumentException("TopicHandler is null Error in messenger")

        topicHandler.addListener { message ->
            val jsonObject = JSONObject(message.content)

        }
        deliver!!.connect("")
    }

    override fun disconnect() {
        deliver?.disconnect()
        deliver = null
    }

}