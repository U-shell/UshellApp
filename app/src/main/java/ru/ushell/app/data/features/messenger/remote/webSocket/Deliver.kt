package ru.ushell.app.data.features.messenger.remote.webSocket

import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import ru.ushell.app.data.common.webSocket.CloseHandler
import ru.ushell.app.data.common.webSocket.WebSocketClient
import ru.ushell.app.data.features.messenger.mappers.MessageRequest

class Deliver(private val senderId: String) : WebSocketClient() {

    fun sendMessage(recipientId: String, message: String) {
        webSocket?.let { ws ->
            val request = MessageRequest(senderId, recipientId, message)
            sendMessage(ws, "/app/chat", Gson().toJson(request))
        }
    }


    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        closeHandler = CloseHandler(webSocket)
    }
}