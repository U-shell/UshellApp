package ru.ushell.app.data.common.webSocket

import okhttp3.WebSocket

data class CloseHandler(
    val socket: WebSocket
) {

    fun close(){
        socket.close(1000,"close webSocket")
    }
}