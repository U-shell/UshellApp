package ru.ushell.app.ui.SavingSession;

import android.content.Context;

public class SaveUser {

    public static boolean isLogin(Context c){
        return Boolean.valueOf(Session.get(c, StaticVar.KEYLOGIN));
    }

    public static void setLogin(Context c){
        Session.save(c, "true", StaticVar.KEYLOGIN);
    }

    public static void userLogOut(Context c){
        Session.save(c,"false", StaticVar.KEYLOGIN);
    }
}
