package com.wiryadev.binarnote.data.repositories

import com.wiryadev.binarnote.data.local.db.BinarNoteDao
import com.wiryadev.binarnote.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: BinarNoteDao
) : NoteRepository {

    override fun getAllNotesByEmail(email: String): Flow<List<NoteEntity>> {
        return noteDao.getAllNotesByEmail(email = email)
    }

    override suspend fun addNote(note: NoteEntity) {
        noteDao.addNote(note = note)
    }

    override suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note = note)
    }

    override suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note = note)
    }

}