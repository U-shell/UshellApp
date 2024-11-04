package ru.ushell.app.api.websocket.socketclient.stomp;

public interface StompMessageListener {

    void onMessage(StompMessage message);

}