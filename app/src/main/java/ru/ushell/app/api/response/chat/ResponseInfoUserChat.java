package ru.ushell.app.api.response.chat;

public class ResponseInfoUserChat {
    private String id;
    private String username;
    private String name;
    private String profilePicture;

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ResponseInfoUserChat(String username, String id, String name, String profilePicture) {
        this.username = username;
        this.id = id;
        this.name = name;
        this.profilePicture = profilePicture;
    }
}
