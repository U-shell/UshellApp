package ru.ushell.app.di

import android.content.Context
import androidx.room.Insert
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.ushell.app.base.UserDatabase
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponentManager::class)
class RoomModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context) =
        Room
            .databaseBuilder(
                context,
                UserDatabase::class.java,
                "user_room_database"
            )
            .allowMainThreadQueries()
            .build()
}