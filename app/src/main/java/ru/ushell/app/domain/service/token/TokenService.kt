package ru.ushell.app.domain.service.token

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenService @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    private val accessToken = "ACCESS"
    private val ttl = "TTL_ACCESS"

    fun saveAccessToken(token: String, time: Long) {
        sharedPreferences.edit {
            putString(accessToken, token)
            putLong(ttl, time)
        }
    }

    fun getAccessToken(): String? = sharedPreferences.getString(accessToken, null)

    fun isTokenValid(): Boolean {
        val expiresAt = sharedPreferences.getLong(ttl, 0)
        return expiresAt > System.currentTimeMillis()
    }

    fun getTimeToken(): Long = sharedPreferences.getLong(ttl, 0)
}