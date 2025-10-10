package ru.ushell.app.data.common.webSocket.stomp

fun interface StompMessageListener {

    fun onMessage(message: StompMessage)

}