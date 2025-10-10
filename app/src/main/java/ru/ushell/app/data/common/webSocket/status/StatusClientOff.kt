package ru.ushell.app.data.common.webSocket.status

import okhttp3.Response
import okhttp3.WebSocket
import ru.ushell.app.data.common.webSocket.CloseHandler
import ru.ushell.app.data.common.webSocket.WebSocketClient

class StatusClientOff: WebSocketClient() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        sendConnectMessage(webSocket)

        for (topic in topics.keys) {
            sendSubscribeMessage(webSocket, topic)
        }

        closeHandler = CloseHandler(webSocket)

    }
}