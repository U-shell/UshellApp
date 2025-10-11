package ru.ushell.app.data.common.webSocket

import ru.ushell.app.data.common.webSocket.stomp.StompMessage
import ru.ushell.app.data.common.webSocket.stomp.StompMessageListener

data class TopicHandler(
    val topic: String,
    val listeners: MutableSet<StompMessageListener> = mutableSetOf()
){
    fun onMessage(message: StompMessage){
        this.listeners.forEach { it.onMessage(message) }
    }

    fun addListener(listener: StompMessageListener){
        this.listeners.add(listener)
    }

    fun removeListener(listener: StompMessageListener){
        this.listeners.remove(listener)
    }
}