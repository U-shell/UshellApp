package ru.ushell.app.data.condition.session

import android.content.Context

class SessionSave {

    fun isLogin(context: Context): Boolean = Session.get(context, SessionStaticVar.KEYLOGGER).toBoolean()

    fun setLogin(context: Context) = Session.save(context, "true",SessionStaticVar.KEYLOGGER)

    fun userLogout(context: Context) = Session.save(context, "false", SessionStaticVar.KEYLOGGER)

}