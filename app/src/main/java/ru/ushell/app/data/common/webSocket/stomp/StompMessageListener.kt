package ru.ushell.app.data.common.webSocket.stomp

interface StompMessageListener {

    fun onMessage(message: StompMessage)

}