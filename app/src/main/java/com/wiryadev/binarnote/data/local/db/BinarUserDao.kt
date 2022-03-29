package com.wiryadev.binarnote.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wiryadev.binarnote.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BinarUserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun register(user: UserEntity)

    @Query("SELECT EXISTS (SELECT 1 FROM tableUser WHERE email=:email AND password=:password)")
    fun login(email: String, password: String): Flow<Int>

    @Query("SELECT EXISTS (SELECT 1 FROM tableUser WHERE email=:email)")
    fun checkUserExist(email: String): Int

}