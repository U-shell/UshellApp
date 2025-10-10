package ru.ushell.app.data.common.webSocket

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import ru.ushell.app.data.common.webSocket.stomp.StompMessage
import ru.ushell.app.data.common.webSocket.stomp.StompMessageSerializer
import java.util.concurrent.TimeUnit

open class WebSocketClient(
    val id: String = "sub-001"
): WebSocketListener() {

    private val destination: String = "destination"
    private val connect: String = "CONNECT"
    private val subscribe: String = "SUBSCRIBE"
    private val send: String = "SEND"


    val topics =  mutableMapOf<String, TopicHandler>()
    var closeHandler: CloseHandler? = null
    var webSocket: WebSocket? = null

    fun subscribe(topic: String): TopicHandler {
        val handler = TopicHandler(topic)
        topics.put(topic, handler)
        return handler
    }

    fun unSubscribe(topic: String){
        topics.remove(topic)
    }

    fun getTopicHandler(topic: String): TopicHandler? {
        if(topics.contains(topic)){
            return topics[topic]
        }
        return null
    }

    fun connect(address: String){
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MICROSECONDS)
            .build()

        val request: Request = Request.Builder()
            .url(address)
            .build()

        client.newWebSocket(request,this)
        client.dispatcher.executorService.shutdown()
    }

    override fun onOpen(webSocket: WebSocket, response: Response){
        this.webSocket = webSocket
        sendConnectMessage(webSocket)

        for(topic in topics.keys){
            sendSubscribeMessage(webSocket,topic)
        }

        closeHandler = CloseHandler(webSocket)
    }

    fun sendConnectMessage(webSocket: WebSocket) =
        webSocket.send(
            StompMessageSerializer.serialize(
                StompMessage(connect)
                    .put("accept-version", "1.1")
                    .put("heart-beat", "10000,10000")
            )
        )


    fun sendSubscribeMessage(webSocket: WebSocket, topic: String) =
        webSocket.send(
            StompMessageSerializer.serialize(
                StompMessage(subscribe)
                    .put("id", id)
                    .put(destination, topic)
            )
        )

    fun disconnect(){
        if(webSocket != null){
            closeHandler?.close()
            webSocket = null
            closeHandler = null
        }
    }

    fun isConnect(): Boolean = closeHandler != null


    override fun onMessage(webSocket: WebSocket, text: String) {
        val message: StompMessage = StompMessageSerializer.deserialize(text)
        val topic = message.getHeader(destination)
        if(topics.contains(topic)) {
            topics[topic]
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000,null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        t.printStackTrace()
    }

    fun sendMessage(webSocket: WebSocket, topic: String, context: String) =
        webSocket.send(
            StompMessageSerializer.serialize(
                StompMessage(send)
                    .put(destination, topic)
                    .setContent(context)
            )
        )

}
