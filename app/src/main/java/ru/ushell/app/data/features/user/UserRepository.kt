package ru.ushell.app.data.features.user

import okhttp3.Credentials
import ru.ushell.app.data.features.user.remote.auth.AuthInfoUserResponse
import ru.ushell.app.data.features.user.room.dao.UserEntity

class UserRepository(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) {

    suspend fun loginUser(email: String, password: String): AuthInfoUserResponse {
        return userRemoteDataSource.getLoginUser(basic = Credentials.basic(email, password))
            .also {
                userLocalDataSource.saveRemoteResponse(it)
            }
    }

    suspend fun getInfoUser(): UserEntity{
        return userLocalDataSource.getInfoUser()
    }

}