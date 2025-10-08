package ru.ushell.app.data.common.service

import android.content.SharedPreferences
import androidx.core.content.edit
import jakarta.inject.Inject

class TokenService @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun saveAccessToken(token: String) {
        sharedPreferences.edit {
            putString("API_KEY", token)
        }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("API_KEY", null)
    }

    fun clearToken() {
        sharedPreferences.edit {
            remove("API_KEY")
        }
    }
}