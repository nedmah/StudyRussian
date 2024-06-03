package com.example.stydyrussian.RoomData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "progress",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Progress(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = 0,

    @ColumnInfo(name = "isCompletedTheory")
    var isCompletedTheory: Boolean = false,

    @ColumnInfo(name = "testProgress")
    var testProgress: Int = 0,

    @ColumnInfo(name = "userId")
    var userId: Int? = null,

    @ColumnInfo(name = "themeNumber")
    var themeNumber: Int? = null

)

