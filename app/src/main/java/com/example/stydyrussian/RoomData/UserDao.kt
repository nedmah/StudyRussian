package com.example.stydyrussian.RoomData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.stydyrussian.UserInfo

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
    @Query("UPDATE users SET name = :name, surname = :surname, email = :email, date = :date WHERE login = :login")
    suspend fun updateUser(login: String, name: String?, surname: String?, email: String?, date: String?)
    // 5) Обновление пароля пользователя по логину
    @Query("UPDATE users SET password = :password WHERE login = :login")
    suspend fun updatePassword(login: String, password: String)
    // 6) Получение данных пользователя по логину
    @Query("SELECT name, surname, email, date FROM users WHERE login = :login")
    suspend fun getUserInfo(login: String): UserInfo?
    // 7) Удаление пользователя по логину
    @Query("DELETE FROM users WHERE login = :login")
    suspend fun deleteUserByLogin(login: String)
    // 8) Получение id по логину
    @Query("SELECT id FROM users WHERE login = :login")
    suspend fun getUserIdByLogin(login: String): Int?

}