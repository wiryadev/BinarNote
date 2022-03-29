package com.wiryadev.binarnote.data.repositories

import com.wiryadev.binarnote.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotesByEmail(email: String): Flow<List<NoteEntity>>

    suspend fun addNote(note: NoteEntity)

    suspend fun updateNote(note: NoteEntity)

    suspend fun deleteNote(note: NoteEntity)

}