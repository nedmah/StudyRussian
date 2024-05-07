package com.example.stydyrussian.RoomData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "progress")
data class Progress(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = 1,

    @ColumnInfo(name = "isCompletedTheory")
    var isCompletedTheory: Boolean = false,

    @ColumnInfo(name = "testProgress")
    var testProgress: Int = 0
) {
    init {
        // Устанавливаем значения по умолчанию для 8 строк
        if (id in 1..8) {
            isCompletedTheory = false
            testProgress = 0
        }
    }
}
