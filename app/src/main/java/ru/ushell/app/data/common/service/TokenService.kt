package ru.ushell.app.data.common.service

import android.content.SharedPreferences
import androidx.core.content.edit
import jakarta.inject.Inject

class TokenService @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {
    private val token: String = "ACCESS"
    private val ttl: String = "TTL_ACCESS"

    fun saveAccessToken(token: String, time: Long) {
        sharedPreferences.edit {
            putString(token, token)
            putLong(ttl,time )
        }
    }

    fun getAccessToken(): String? =  sharedPreferences.getString(token, null)

    fun isTokenValid(): Boolean {
        val expiresAt = sharedPreferences.getLong(ttl, 0)
        return expiresAt > System.currentTimeMillis()
    }

    fun getTimeToken(): Long = sharedPreferences.getLong(ttl,0)

}