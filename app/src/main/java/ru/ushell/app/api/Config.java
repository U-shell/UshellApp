package ru.ushell.app.api;

public class Config {

//      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
//        Torfiks
    private final static String protocol = "http";
    private final static String domain = "192.168.1.78";
    private final static String port = "8082";

    //TODO сделать подключние по gate-way
    public static final String webSocketAddress = String.format("ws://%s:%s/ws/websocket", domain, port);
    public static final String webSocketAddressQR = String.format("ws://%s:%s/loginListener/websocket", domain, port);

    public static String getUrl(){
        return String.format("%s://%s:%s",protocol,domain,port);
    }

    public static String getHost(){
        return String.format("%s:%s",domain,port);
    }

}
