package ru.ushell.app.data.common.webSocket.stomp;

import java.util.HashMap;
import java.util.Map;


public class StompMessage {

    private final Map<String, String> headers = new HashMap<>();
    private String body = "";
    private String command;

    public StompMessage() {}

    public StompMessage(String command) {
        this.command = command;
    }

    public String getHeader(String name){
        return headers.get(name);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public StompMessage put(String name, String value){
        headers.put(name, value);
        return this;
    }

    public StompMessage setContent(String body){
        this.body = body;
        return this;
    }

    public String getContent() {
        return body;
    }

    public String getCommand() {
        return command;
    }

    static class Body{
        private String id;
        private String senderId;
        private String senderName;
        private String message;
    }
}

