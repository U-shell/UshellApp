package ru.ushell.app.api.body;

import ru.ushell.app.models.User;

public class BodyRequestTokenIdentifier {

    private String accessToken;
    private String typeToken;

    public String getAccessToken() {
        return accessToken;
    }

    public BodyRequestTokenIdentifier setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getTypeToken() {
        return typeToken;
    }

    public BodyRequestTokenIdentifier setTypeToken(String typeToken) {
        this.typeToken = typeToken;
        return this;
    }

    public String setToken(String type, String access){
        return String.format("%s %s",type, access);
    }

    public String getToken(){
        return String.format("%s %s", User.getTypeToken(), User.getAccessToken());
    }

}
