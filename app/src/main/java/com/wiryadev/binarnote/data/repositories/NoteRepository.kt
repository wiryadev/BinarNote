package com.wiryadev.binarnote.data.repositories

import com.wiryadev.binarnote.data.Result
import com.wiryadev.binarnote.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotesByEmail(email: String): Flow<List<NoteEntity>>

    suspend fun addNote(note: NoteEntity): Result<Long>

    suspend fun updateNote(note: NoteEntity): Result<Int>

    suspend fun deleteNote(note: NoteEntity): Result<Int>

}