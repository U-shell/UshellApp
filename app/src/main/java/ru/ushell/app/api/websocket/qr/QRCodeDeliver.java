package ru.ushell.app.api.websocket.qr;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.WebSocket;
import ru.ushell.app.api.websocket.socketclient.CloseHandler;
import ru.ushell.app.api.websocket.socketclient.SpringBootWebSocketClient;
import ru.ushell.app.models.User;

public class QRCodeDeliver extends SpringBootWebSocketClient {

    private String code;

    public QRCodeDeliver(){}

    public QRCodeDeliver(String code) {
        this.code = code;
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        super.onOpen(webSocket, response);

        String room;
        String code_;

        try {
            if(code != null) {
                JSONObject jsonObject = new JSONObject(code);

                room = jsonObject.getString("room");
                code_ = jsonObject.getString("code");

                String destination = "/app/login/" + room;
                Gson gson = new Gson();

                String json = gson.toJson(
                        new QRCodeRequest(
                                room,
                                code_,
                                User.getAccessToken(),
                                "MOBILE"
                        ));
                sendMessage(
                        webSocket,
                        destination,
                        json
                );

                closeHandler = new CloseHandler(webSocket);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
