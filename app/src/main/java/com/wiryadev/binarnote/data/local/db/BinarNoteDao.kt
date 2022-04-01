package com.wiryadev.binarnote.data.local.db

import androidx.room.*
import com.wiryadev.binarnote.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BinarNoteDao {

    @Query("SELECT * FROM tableNote WHERE owner=:email")
    fun getAllNotesByEmail(email: String): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity): Int

    @Delete
    suspend fun deleteNote(note: NoteEntity): Int

}