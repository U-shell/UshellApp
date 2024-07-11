package ru.ushell.app.api;

public class Config {

//      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
//        Torfiks
    private final static String protocol = "http";
    private final static String domain = "ushell.ru";
    private final static String port = "8080";

    public static String getProtocol() {
        return protocol;
    }

    public static String getDomain() {
        return domain;
    }

    public static String getPort() {
        return port;
    }

    public static String getUrl(){
        return String.format("%s://%s:%s",protocol,domain,port);
    }
}
