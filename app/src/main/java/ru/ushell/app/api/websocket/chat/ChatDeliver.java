package ru.ushell.app.api.websocket.chat;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import okhttp3.Response;
import okhttp3.WebSocket;
import ru.ushell.app.api.websocket.socketclient.CloseHandler;
import ru.ushell.app.api.websocket.socketclient.SpringBootWebSocketClient;

/**
 * Отправка сообещний  и получение сообщений
 */
public class ChatDeliver extends SpringBootWebSocketClient {

    private String senderId;
    private String recipientId;
    private String message;

    public ChatDeliver(){}

    public ChatDeliver(String senderID, String recipientId, String content) {
        this.senderId = senderID;
        this.recipientId = recipientId;
        this.message = content;
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        super.onOpen(webSocket, response);

        String destination = "/app/chat";

        Gson gson = new Gson();

        String json = gson.toJson(
                new ChatMessageRequest(
                senderId,
                recipientId,
                message
        ));

        sendMessage(
                webSocket,
                destination,
                json
        );

        closeHandler = new CloseHandler(webSocket);
    }
}
