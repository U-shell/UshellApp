package ru.ushell.app.api.request

import android.content.Context
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Credentials.basic
import retrofit2.Call
import retrofit2.Callback
import ru.ushell.app.api.API.api
import ru.ushell.app.api.Service
import ru.ushell.app.api.response.ResponseLogin
import ru.ushell.app.api.response.info.ResponseInfoUser
import ru.ushell.app.models.User
import ru.ushell.app.utils.SavingSession.SaveUser
import java.net.HttpURLConnection
import kotlin.coroutines.resume

interface AuthorizationCallback {
    fun onAuthReceived(auth: Boolean)
}

fun loginUser(
    email: String?,
    password: String?,
    context: Context,
    callback: AuthorizationCallback?
){
    val auth = basic(email ?: "", password ?: "")

    val response = api.login(auth)

    response.enqueue (object : Callback<ResponseLogin> {
        override fun onResponse(
            call: Call<ResponseLogin?>,
            response: retrofit2.Response<ResponseLogin?>
        ) {
            when{
                response.isSuccessful && response.body() != null ->{
                    val userData = response.body()!!
                    val user = User(context)
                    user.saveUserDate(userData) {
                        SaveUser.setLogin(context)
                        Service(context).buildData()
                    }
                    callback?.onAuthReceived(true)
                }

                response.code() == HttpURLConnection.HTTP_UNAUTHORIZED ->{
                    callback?.onAuthReceived(false)
                }
            }
        }

        override fun onFailure(
            call: Call<ResponseLogin?>,
            t: Throwable
        ) {
            callback?.onAuthReceived(false)
        }
    })
}

suspend fun logoutUser(){

}

fun refreshToken(
    refreshToken: String,
    context: Context
){
    val response = api.refreshToken("Bearer $refreshToken")

    response.enqueue (object : Callback<ResponseInfoUser> {
        override fun onResponse(
            call: Call<ResponseInfoUser?>,
            response: retrofit2.Response<ResponseInfoUser?>
        ) {
            when{
                response.isSuccessful && response.body() != null ->{
                    val userData = response.body()!!
                    val user = User(context)
                    user.addTokenUser(userData)
                }

                response.code() == HttpURLConnection.HTTP_UNAUTHORIZED ->{
                }
            }
        }

        override fun onFailure(
            call: Call<ResponseInfoUser?>,
            t: Throwable
        ) {
        }
    })

}

