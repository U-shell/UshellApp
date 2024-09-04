package ru.ushell.app.utils.SavingSession;

import android.content.Context;

public class SaveUser {

    public static boolean isLogin(Context c){
        return Boolean.parseBoolean(Session.get(c, StaticVar.KEYLOGGER));
    }

    public static void setLogin(Context c){
        Session.save(c, "true", StaticVar.KEYLOGGER);
    }

    public static void userLogOut(Context c){
        Session.save(c,"false", StaticVar.KEYLOGGER);
    }
}
