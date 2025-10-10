package ru.ushell.app.data.features.messanger.remote.webSocket

import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import ru.ushell.app.data.common.webSocket.CloseHandler
import ru.ushell.app.data.common.webSocket.WebSocketClient
import ru.ushell.app.data.features.messanger.dto.MessageRequest

class Deliver(
    val senderId: String,
    val recipientId: String,
    val message: String,
): WebSocketClient() {

    constructor() : this("", "", "")

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        val topic = "/app/chat"

        sendMessage(
            webSocket,
            topic,
            Gson().toJson(
                MessageRequest(
                    senderId = senderId,
                    recipientId = recipientId,
                    message = message
                )
            )
        )

        closeHandler = CloseHandler(webSocket)
    }
}