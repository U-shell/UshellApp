package ru.ushell.app.api.body;

public class BodyRequestSingIn {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public BodyRequestSingIn setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public BodyRequestSingIn setPassword(String password) {
        this.password = password;
        return this;
    }

}
