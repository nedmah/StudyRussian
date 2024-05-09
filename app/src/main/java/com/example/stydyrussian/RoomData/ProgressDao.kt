package com.example.stydyrussian.RoomData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stydyrussian.TestScore


@Dao
interface ProgressDao {

    //Обновляем значение прохождения теории и номер этой темы где id = ""
    @Query("UPDATE progress SET isCompletedTheory = :newIsCompletedTheory, themeNumber = :newThemeNumber WHERE userId = :userId")
    suspend fun updateProgress(userId: Int, newIsCompletedTheory: Boolean, newThemeNumber: Int)

    // Запись прогресса в базу данных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: Progress)

    //Получаем номера тем где пройдена теория и userId =""
    @Query("SELECT themeNumber FROM progress WHERE isCompletedTheory = 1 AND userId = :userId")
    suspend fun getCompletedThemeNumbers(userId: Int): List<Int>

    //Получаем прогресс тестов
    @Query("SELECT themeNumber, testProgress FROM progress WHERE testProgress > 0 AND userId = :userId")
    suspend fun getTestScore(userId: Int): List<TestScore>

    //Получаем номер темы для отображения теста по id пользователя и где testProgress>7
    @Query("SELECT themeNumber FROM progress WHERE testProgress >= 7 AND userId = :userId")
    suspend fun getTestProgressMoreThan7(userId: Int): List<Int>

    //обновляем прогресс теста
    @Query("UPDATE progress SET testProgress = :newTestProgress WHERE userId = :userId AND themeNumber = :themeNumber")
    suspend fun updateTestProgress(userId: Int, themeNumber: Int, newTestProgress: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestProgress(progress: Progress)

    //чекаем есть запись с такой темой и таким юзером уже или нет
    @Query("SELECT * FROM progress WHERE userId = :userId AND themeNumber = :themeNumber")
    suspend fun getProgressByUserAndTheme(userId: Int, themeNumber: Int): Progress?

    //удаляем весь прогресс
    @Query("DELETE FROM progress WHERE userId = :userId")
    suspend fun deleteProgressByUserId(userId: Int)

}