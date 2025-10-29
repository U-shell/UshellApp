package ru.ushell.app.data.features.user

import okhttp3.Credentials
import ru.ushell.app.data.common.service.TokenService
import ru.ushell.app.data.features.user.remote.webSocket.Connect
import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse
import ru.ushell.app.data.features.user.room.dao.UserEntity

class UserRepository(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenService: TokenService,
) {
    private var connect: Connect? = null

    suspend fun activeUser(): Boolean {
        return userLocalDataSource.activeUser()
    }

    suspend fun loginUser(email: String, password: String) =
        userRemoteDataSource.getLoginUser(basic = Credentials.basic(email, password))
            .also {
                userLocalDataSource.saveRemoteResponse(it)
                tokenService.saveAccessToken(it.accessToken)
            }


    suspend fun logoutUser() =  userLocalDataSource.logoutUser(username = getUsername())

    suspend fun getAccessToke() = userLocalDataSource.getAccessToken()

    suspend fun getInfoUser(): UserEntity = userLocalDataSource.getInfoUser()

    suspend fun getUsername(): String = userLocalDataSource.getUsername()

    suspend fun getGroupId(): Int = userLocalDataSource.getGroupId()

    suspend fun setChatId(chatId: String) = userLocalDataSource.setChatId(chatId)

    suspend fun getChatId(): String = userLocalDataSource.getChaId()

    suspend fun connectWebSocket() {
        if (connect?.isConnected() == true) return
        connect = Connect()
        connect?.connect()
        val isConnected = connect?.awaitConnection() == true
        if (!isConnected) {
            throw IllegalStateException("Failed to connect WebSocket")
        }
    }

    suspend fun disconnectWebSocket() {
        connect?.disconnect()
        connect = null
    }

    suspend fun sendMessage(code: String) {
        // isConnected проверка уже не нужна, если connectWebSocket гарантирует подключение
        connect?.sendQrMessage(code, getAccessToke())
    }


}