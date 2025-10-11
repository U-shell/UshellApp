package ru.ushell.app.data.features.user.remote.webSocket

import com.google.gson.Gson
import okhttp3.Response
import org.json.JSONObject
import ru.ushell.app.data.common.webSocket.CloseHandler
import ru.ushell.app.data.common.webSocket.WebSocket
import ru.ushell.app.data.common.webSocket.WebSocketClient

class Deliver(
    val code: String,
): WebSocketClient() {
    constructor() : this("")

    override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        val room: String
        val codeSecurity: String

        if (code != null){
            val json = JSONObject(code)

            room = json.getString("room")
            codeSecurity = json.getString("code")

            val topic = "/app/login/$room"
            val gson = Gson()

            sendMessage(
                webSocket, topic, gson.toJson(
                    QRCodeRequest(
                        room,
                        code,
                        "1",
                        "MOBILE"
                    )
                )
            )
            closeHandler = CloseHandler(webSocket)
        }
    }

}