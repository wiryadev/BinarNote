package com.wiryadev.binarnote.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wiryadev.binarnote.data.local.entity.NoteEntity
import com.wiryadev.binarnote.data.local.entity.UserEntity

@Database(
    entities = [
        NoteEntity::class,
        UserEntity::class,
    ],
    version = 1,
)
abstract class BinarNoteDatabase : RoomDatabase() {
    abstract fun noteDao(): BinarNoteDao
    abstract fun userDao(): BinarUserDao
}