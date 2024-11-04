package ru.ushell.app.api.websocket.socketclient;

import java.util.HashSet;
import java.util.Set;

import ru.ushell.app.api.websocket.socketclient.stomp.StompMessage;
import ru.ushell.app.api.websocket.socketclient.stomp.StompMessageListener;

public class TopicHandler {

    private String topic;
    private final Set<StompMessageListener> listeners = new HashSet<>();

    public TopicHandler() {}

    public TopicHandler(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void addListener(StompMessageListener listener) {
        listeners.add(listener);
    }

    public void removeListener(StompMessageListener listener) {
        listeners.remove(listener);
    }

    public void onMessage(StompMessage message){
        for (StompMessageListener listener : listeners) {
            listener.onMessage(message);
        }
    }
}
