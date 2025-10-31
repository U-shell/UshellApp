package ru.ushell.app.data.features.user.remote.webSocket

import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import ru.ushell.app.data.common.webSocket.CloseHandler
import ru.ushell.app.data.common.webSocket.WebSocketClient

class Deliver(
    private val onConnected: (() -> Unit)? = null
) : WebSocketClient() {

    fun sendMessage(request: QRCodeRequest) {
        webSocket?.let { ws ->
            sendMessage(ws, "/app/login/${request.room}", Gson().toJson(request))
        }
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        closeHandler = CloseHandler(webSocket)
        onConnected?.invoke()
    }
}