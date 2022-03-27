package com.wiryadev.binarnote.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tableUser")
data class UserEntity(
    @PrimaryKey
    val email: String,
    val username: String,
    val password: String,
)
