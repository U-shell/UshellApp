package ru.ushell.app.api.response.chat;

import java.util.Date;

public class ResponseMessageChat {
    private String messageId;

    private String senderId;
    private String message;
    private String status;
    private String timestamp;
    // заняться дополнительными методами
    private String replyIdMessage;
    private String forwardIdMessage;

    private Boolean edit;

    public ResponseMessageChat(String messageId, String senderId, String message, String status, String timestamp, String replyIdMessage, String forwardIdMessage, Boolean edit) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.replyIdMessage = replyIdMessage;
        this.forwardIdMessage = forwardIdMessage;
        this.edit = edit;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public String getForwardIdMessage() {
        return forwardIdMessage;
    }

    public void setForwardIdMessage(String forwardIdMessage) {
        this.forwardIdMessage = forwardIdMessage;
    }

    public String getReplyIdMessage() {
        return replyIdMessage;
    }

    public void setReplyIdMessage(String replyIdMessage) {
        this.replyIdMessage = replyIdMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
