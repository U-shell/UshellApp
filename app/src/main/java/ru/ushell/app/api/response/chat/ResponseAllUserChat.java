package ru.ushell.app.api.response.chat;

import java.util.ArrayList;

public class ResponseAllUserChat {

    private ArrayList<ResponseInfoUserChat> listUsers;

    public ResponseAllUserChat(ArrayList<ResponseInfoUserChat> lists) {
        this.listUsers = lists;
    }

    public ArrayList<ResponseInfoUserChat> getList() {
        return listUsers;
    }

    public void setList(ArrayList<ResponseInfoUserChat> list) {
        this.listUsers = list;
    }
}

