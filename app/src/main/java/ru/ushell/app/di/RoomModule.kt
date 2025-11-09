package ru.ushell.app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.ushell.app.base.UserDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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
            .fallbackToDestructiveMigration(true)
            .allowMainThreadQueries()
            .build()
}