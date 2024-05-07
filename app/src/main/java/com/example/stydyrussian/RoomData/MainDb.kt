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




    companion object{
        @Volatile
        private var INSTANCE: MainDb? = null

//        fun getDb(context: Context): MainDb {
//            return Room.databaseBuilder(
//                context,MainDb::class.java,
//                "Verbum.db"
//            ).build()
//        }

        fun getDb(context: Context): MainDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDb::class.java,
                    "Verbum.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }


    }

}