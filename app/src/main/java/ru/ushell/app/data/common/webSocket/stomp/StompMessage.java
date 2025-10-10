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
        return null;
    }
    public StompMessage setContent(String body){
        this.body = body;
        return null;
    }

    public String getContent() {
        return body;
    }
    public String getCommand() {
        return command;
    }

}

