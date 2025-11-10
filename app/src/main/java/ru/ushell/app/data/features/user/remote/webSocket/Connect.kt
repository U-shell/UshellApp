package ru.ushell.app.data.features.user.remote.webSocket

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeoutOrNull
import org.json.JSONObject
import ru.ushell.app.data.common.webSocket.TopicHandler
import ru.ushell.app.data.common.webSocket.WebSocketConnect

class Connect(
    val username: String
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
        val topic = "/topic/qr/${username}"

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
        deliver!!.connect(url = "/qr")
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

    fun  sendFileMessage(qrCode:String, senderId:String, resenderId:String, fileName:String){
        val jsonObject = JSONObject(qrCode)
        val room = jsonObject.getString("room")
        val code = jsonObject.getString("code")

        deliver?.sendMessage(
            QRCodeRequest(
                room = room,
                code = code,
                message = "$senderId,$resenderId,$fileName",
                "FILE")
        )
    }

    fun isConnected(): Boolean = deliver?.isConnect() == true

    suspend fun awaitConnection(timeout: Long = 5000L): Boolean {
        return withTimeoutOrNull(timeout) {
            connectionReady.await()
        } ?: false
    }
}
