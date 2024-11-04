package ru.ushell.app.api.websocket.qr;

public class QRCodeRequest {

    private String room;
    private String code;
    private String message;
    private String type;

    public QRCodeRequest(String room, String code, String message, String type) {
        this.room = room;
        this.code = code;
        this.message = message;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
