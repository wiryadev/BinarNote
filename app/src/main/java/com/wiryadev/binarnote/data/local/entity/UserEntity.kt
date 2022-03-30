package com.wiryadev.binarnote.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tableUser",
    indices = [
        Index(
            value = [
                "username"
            ],
            unique = true,
        ),
    ]
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    val password: String,
)
