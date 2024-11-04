package ru.ushell.app.models.modelChat.chat;

import java.util.ArrayList;

public class Chat {

    public static ArrayList<Chat> ChatList = new ArrayList<>();

    private String username;
    private String name;
    private String recipientId;

    private Integer countNewMessage;

    public Chat(String username, String name, String recipientId, Integer countNewMessage) {
        this.username = username;
        this.name = name;
        this.recipientId = recipientId;
        this.countNewMessage = countNewMessage;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountNewMessage(Integer countNewMessage) {
        this.countNewMessage = countNewMessage;
    }

    public Integer getCountNewMessage() {
        return countNewMessage;
    }

}
