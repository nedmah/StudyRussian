package com.example.stydyrussian.RoomData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database (
    entities = [User::class,Progress::class],
    version = 1
)
abstract class MainDb : RoomDatabase() {
    abstract fun getUsersDao() : UserDao
    abstract fun getProgressDao() : ProgressDao

}