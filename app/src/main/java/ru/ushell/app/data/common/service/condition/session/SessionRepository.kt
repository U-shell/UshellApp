package ru.ushell.app.data.common.service.condition.session

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionRepository {
    companion object {

        private const val CONDITION = "condition"

        fun save(context: Context, value: String, key: String){
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(CONDITION, Context.MODE_PRIVATE)

            sharedPreferences.edit {
                putString(key, value)
            }
        }

        fun get(context: Context, key: String): String? {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(CONDITION, Context.MODE_PRIVATE)

            return sharedPreferences.getString(key, "false")
        }
    }
}