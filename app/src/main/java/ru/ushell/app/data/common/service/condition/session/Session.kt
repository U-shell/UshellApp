package ru.ushell.app.data.common.service.condition.session

import android.content.Context

class Session {
    companion object {

        fun isLogin(context: Context): Boolean =
            SessionRepository.get(context, SessionStaticVar.KEYLOGGER).toBoolean()

        fun setLogin(context: Context) =
            SessionRepository.save(context, "true", SessionStaticVar.KEYLOGGER)

        fun userLogout(context: Context) =
            SessionRepository.save(context, "false", SessionStaticVar.KEYLOGGER)
    }
}