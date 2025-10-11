package ru.ushell.app.data.features.user.remote.webSocket

data class QRCodeRequest(
    val room: String,
    val code:String,
    val message: String,
    val type: String
)