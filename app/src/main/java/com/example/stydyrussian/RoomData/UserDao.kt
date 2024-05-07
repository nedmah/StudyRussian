package com.example.stydyrussian.RoomData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    // 1) Проверка наличия логина в базе данных
    @Query("SELECT COUNT(*) FROM users WHERE login = :login")
    suspend fun checkLoginExists(login: String): Int

    // 2) Проверка совпадения логина и пароля
    @Query("SELECT COUNT(*) FROM users WHERE login = :login AND password = :password")
    suspend fun checkLoginAndPassword(login: String, password: String): Int

    // 3) Запись пользователя в базу данных
    @Insert
    suspend fun insertUser(user: User)

    // 4) Обновление данных пользователя
    @Update
    suspend fun updateUser(user: User)

    // 5) Обновление пароля пользователя по логину
    @Query("UPDATE users SET password = :password WHERE login = :login")
    suspend fun updatePassword(login: String, password: String)
}