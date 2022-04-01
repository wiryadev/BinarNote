package com.wiryadev.binarnote.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tableNote")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "logbook")
    var logbook: String,

    @ColumnInfo(name = "owner")
    val owner: String,
)
