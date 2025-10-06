package ru.ushell.app.data.features.user.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ushell.app.data.features.user.room.dao.UserEntity.Companion.TABLE_NAME

@Dao
interface UserDao {

    @Query("SELECT EXISTS(SELECT active FROM $TABLE_NAME WHERE active = 1)")
    suspend fun activeUser(): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun saveUser(userEntity: UserEntity)

    @Query("UPDATE $TABLE_NAME SET active = 1 WHERE username =:username")
    suspend fun setStatusActive(username: String)

    @Query("UPDATE $TABLE_NAME SET active = 0 WHERE username =:username")
    suspend fun setStatusNoActive(username: String)

    @Query("SELECT * FROM $TABLE_NAME WHERE active = 1")
    suspend fun getInfoUser(): UserEntity

    @Query("SELECT groupId FROM $TABLE_NAME WHERE active = 1")
    suspend fun getGroupId(): Int
}