package ru.ushell.app.data.features.user.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface UserDao {

//    @Insert(entity = UserEntity::class, onConflict = OnConflictStrategy.REPLACE) // если пришли теже данные, то просто перезапистаь
//    suspend fun saveUser(userEntity: UserEntity)
}