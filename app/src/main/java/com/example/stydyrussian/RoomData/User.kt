package com.example.stydyrussian.RoomData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = 0,

    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "surname")
    var surname: String? = null,

    @ColumnInfo(name = "email")
    var email: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null
)
