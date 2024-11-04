package ru.ushell.app.api.websocket.qr;

import org.json.JSONException;
import org.json.JSONObject;
import ru.ushell.app.api.Config;
import ru.ushell.app.api.websocket.socketclient.TopicHandler;
import ru.ushell.app.api.websocket.socketclient.stomp.StompMessage;
import ru.ushell.app.api.websocket.socketclient.stomp.StompMessageListener;

//TODO: сделать проверку подключения
public class QRCodeConnect {

    static QRCodeDeliver qrCodeDeliver;

    public void connect(){
        qrCodeDeliver = new QRCodeDeliver();
        TopicHandler topicHandler = qrCodeDeliver.subscribe(
                "/topic/loginListener/" + "qwe");

        topicHandler.addListener(new StompMessageListener() {
            @Override
            public void onMessage(StompMessage message){

                JSONObject jsonObject;
                String msg;
                System.out.println(message);

                try {
                    jsonObject = new JSONObject(message.getContent());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        qrCodeDeliver.connect(Config.webSocketAddressQR);
    }

    public void disconnect(){
        qrCodeDeliver.disconnect();
    }
}
