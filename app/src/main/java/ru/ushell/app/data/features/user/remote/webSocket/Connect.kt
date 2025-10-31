package ru.ushell.app.data.features.user.remote.webSocket

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeoutOrNull
import org.json.JSONObject
import ru.ushell.app.data.common.webSocket.TopicHandler
import ru.ushell.app.data.common.webSocket.WebSocketConnect

class Connect(
    override val url: String = "ws://192.168.1.78:8082/qr",
) : WebSocketConnect {
    private val connectionReady = CompletableDeferred<Boolean>()

    private var deliver: Deliver? = null

    override fun connect() {

        if (deliver == null) {
            deliver = Deliver{
                connectionReady.complete(true)
            }
        }
        // добавть username в топик
        val topic = "/topic/qr/1290902"

        val topicHandler: TopicHandler = deliver!!.subscribe(topic)
        topicHandler.addListener { stompMessage ->
            try {
                val json = JSONObject(stompMessage.content)
                println(json)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Подключаемся
        deliver!!.connect(url)
    }

    override fun disconnect() {
        deliver?.disconnect()
        deliver = null
    }

    fun sendQrMessage(qrCode:String, token:String) {
        val jsonObject = JSONObject(qrCode)
        val room = jsonObject.getString("room")
        val code = jsonObject.getString("code")

        deliver?.sendMessage(
            QRCodeRequest(
                room = room,
                code = code,
                message = token,
                "MOBILE")
        )

    }

    fun isConnected(): Boolean = deliver?.isConnect() == true

    suspend fun awaitConnection(timeout: Long = 5000L): Boolean {
        return withTimeoutOrNull(timeout) {
            connectionReady.await()
        } ?: false
    }
}
