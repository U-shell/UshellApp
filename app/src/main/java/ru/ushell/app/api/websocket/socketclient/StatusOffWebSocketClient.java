package ru.ushell.app.api.websocket.socketclient;


import androidx.annotation.NonNull;

import okhttp3.Response;
import okhttp3.WebSocket;

public class StatusOffWebSocketClient extends SpringBootWebSocketClient {
    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        super.onOpen(webSocket, response);
        sendConnectMessage(webSocket);
        for (String topic : topics.keySet()) {
            sendSubscribeMessage(webSocket, topic);
        }
        // turn the givenId user status off
//        sendMessage(webSocket, "/app/userList/status/off", "1");
        closeHandler = new CloseHandler(webSocket);
    }
}