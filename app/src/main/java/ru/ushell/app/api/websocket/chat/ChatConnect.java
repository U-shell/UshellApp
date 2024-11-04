package ru.ushell.app.api.websocket.chat;


import org.json.JSONException;
import org.json.JSONObject;

import java.time.OffsetDateTime;

import ru.ushell.app.api.Config;
import ru.ushell.app.api.websocket.socketclient.stomp.StompMessage;
import ru.ushell.app.api.websocket.socketclient.stomp.StompMessageListener;
import ru.ushell.app.api.websocket.socketclient.TopicHandler;
import ru.ushell.app.models.User;
import ru.ushell.app.models.modelChat.message.Message;
import ru.ushell.app.models.modelChat.message.MessageList;

public class ChatConnect {

    static ChatDeliver chatListener;

    public void connect(){
        chatListener = new ChatDeliver();
        TopicHandler topicHandler = chatListener.subscribe(
                "/user/" + User.getKeyIdUserChat() + "/queue/messages");

        topicHandler.addListener(new StompMessageListener() {
            @Override
            public void onMessage(StompMessage message){

                JSONObject jsonObject;
                String msg;

                try {
                    jsonObject = new JSONObject(message.getContent());
                    msg = jsonObject.getString("message");
                    boolean auth = jsonObject.getString("senderId").equals(User.getKeyIdUserChat());
                    MessageList.addMessages(
                            new Message(
                                    auth,
                                    msg,
                                    OffsetDateTime.now()
                            )
                    );
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        chatListener.connect(Config.webSocketAddress);
    }
}
