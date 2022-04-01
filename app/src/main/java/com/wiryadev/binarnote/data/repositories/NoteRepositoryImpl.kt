package com.wiryadev.binarnote.data.repositories

import com.wiryadev.binarnote.data.Result
import com.wiryadev.binarnote.data.local.db.BinarNoteDao
import com.wiryadev.binarnote.data.local.entity.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: BinarNoteDao
) : NoteRepository {

    override fun getAllNotesByEmail(email: String): Flow<List<NoteEntity>> {
        return noteDao.getAllNotesByEmail(email = email)
    }

    override suspend fun addNote(note: NoteEntity): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val result = noteDao.addNote(note = note)
                Result.Success(result)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun updateNote(note: NoteEntity): Result<Int> {
        return withContext(Dispatchers.IO) {
            try {
                val result = noteDao.updateNote(note = note)
                Result.Success(result)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun deleteNote(note: NoteEntity): Result<Int> {
        return withContext(Dispatchers.IO) {
            try {
                val result = noteDao.deleteNote(note = note)
                Result.Success(result)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

}