package ru.ushell.app.data.features.user.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.ushell.app.data.features.user.room.dao.UserEntity.Companion.TABLE_NAME

@Dao
interface UserDao {

//    @Insert(entity = UserEntity::class, onConflict = OnConflictStrategy.REPLACE) // если пришли теже данные, то просто перезапистаь
    @Insert
    @JvmSuppressWildcards
    suspend fun saveUser(userEntity: UserEntity)

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getInfoUser(): UserEntity
}