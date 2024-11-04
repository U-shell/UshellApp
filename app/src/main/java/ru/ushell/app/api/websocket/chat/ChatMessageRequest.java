package ru.ushell.app.api.websocket.chat;

public class ChatMessageRequest {

    private String message;
    private String senderId;
    private String recipientId;

    public ChatMessageRequest(String senderId, String recipientId,String message) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
}
