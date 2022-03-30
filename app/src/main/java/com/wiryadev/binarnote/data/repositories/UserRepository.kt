package com.wiryadev.binarnote.data.repositories

import com.wiryadev.binarnote.data.local.entity.UserEntity
import com.wiryadev.binarnote.data.preference.AuthModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun register(user: UserEntity)

    suspend fun login(email: String, password: String):Flow<UserEntity>

    suspend fun checkUserExist(email: String): Int

    suspend fun getUserSession(): Flow<AuthModel>

    suspend fun saveUserSession(user: AuthModel)

    suspend fun deleteUserSession()

}