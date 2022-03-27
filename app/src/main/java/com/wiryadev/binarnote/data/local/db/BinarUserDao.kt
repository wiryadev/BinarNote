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
    suspend fun registerUser(user: UserEntity)

    @Query("SELECT EXISTS (SELECT 1 FROM tableUser WHERE email=:email)")
    fun checkUserExist(email: String): Flow<Int>

}