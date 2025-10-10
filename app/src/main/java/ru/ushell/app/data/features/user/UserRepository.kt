package ru.ushell.app.data.features.user

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import okhttp3.Credentials
import ru.ushell.app.data.common.service.TokenService
import ru.ushell.app.data.condition.session.Session
import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse
import ru.ushell.app.data.features.user.room.dao.UserEntity

class UserRepository(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenService: TokenService,
) {
    suspend fun activeUser(): Boolean {
        return userLocalDataSource.activeUser()
    }

    suspend fun loginUser(email: String, password: String, context: Context): AuthInfoUserResponse {
        return userRemoteDataSource.getLoginUser(basic = Credentials.basic(email, password))
            .also {
                userLocalDataSource.saveRemoteResponse(it)
                Session.setLogin(context)
                tokenService.saveAccessToken(it.accessToken)
            }
    }

    suspend fun getInfoUser(): UserEntity = userLocalDataSource.getInfoUser()

    suspend fun getUsername(): String = userLocalDataSource.getUsername()

    suspend fun getGroupId(): Int = userLocalDataSource.getGroupId()

    suspend fun setChatId(chatId: String) = userLocalDataSource.setChatId(chatId)

    suspend fun getChatId(): String = userLocalDataSource.getChaId()
}