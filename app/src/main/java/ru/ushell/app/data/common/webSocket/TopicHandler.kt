package ru.ushell.app.data.common.webSocket

import ru.ushell.app.data.common.webSocket.stomp.StompMessage
import ru.ushell.app.data.common.webSocket.stomp.StompMessageListener

data class TopicHandler(
    val topic: String,
    val listeners: Set<StompMessageListener> = HashSet()
){
    fun onMessage(message: StompMessage){
        for (listener:StompMessageListener in this.listeners){
            listener.onMessage(message)
        }
    }
}