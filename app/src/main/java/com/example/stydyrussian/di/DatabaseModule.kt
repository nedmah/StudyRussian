package com.example.stydyrussian.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.stydyrussian.RoomData.MainDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMainDb(app: Application) : MainDb {
       return Room.databaseBuilder(
            app,
           MainDb::class.java,
           "Verbum.db"
       ).build()
    }

    @Provides
    @Singleton
    fun provideSharePrefs(app: Application) : SharedPreferences{
        return app.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
    }
}