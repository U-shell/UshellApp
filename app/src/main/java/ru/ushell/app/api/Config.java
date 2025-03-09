package ru.ushell.app.api;

public class Config {

//      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
//        Torfiks
    private final static String protocol = "https";
    private final static String domain = "api.ushell.ru";
    //TODO сделать подключние по gate-way
    public static final String webSocketAddress = String.format("ws://%s/ws/websocket", domain);
    public static final String webSocketAddressQR = String.format("ws://%s/loginListener/websocket", domain);

    public static String getUrl(){
        return String.format("%s://%s",protocol,domain);
    }


}
