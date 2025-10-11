package ru.ushell.app.data.common.webSocket

interface WebSocketConnect {

    val url: String

    fun connect()

    fun disconnect()

}