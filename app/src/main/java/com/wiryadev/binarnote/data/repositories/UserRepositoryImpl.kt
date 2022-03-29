package com.wiryadev.binarnote.data.repositories

import com.wiryadev.binarnote.data.local.db.BinarUserDao
import com.wiryadev.binarnote.data.local.entity.UserEntity
import com.wiryadev.binarnote.data.preference.AuthModel
import com.wiryadev.binarnote.data.preference.AuthPreference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: BinarUserDao,
    private val preference: AuthPreference,
) : UserRepository {

    override suspend fun register(user: UserEntity) {
        userDao.register(user = user)
    }

    override suspend fun login(email: String, password: String): Flow<Int> {
        return userDao.login(
            email = email,
            password = password,
        )
    }

    override suspend fun checkUserExist(email: String): Int {
        return userDao.checkUserExist(email = email)
    }

    override suspend fun getUserSession(): Flow<AuthModel> {
        return preference.getUserSession()
    }

    override suspend fun saveUserSession(user: AuthModel) {
        preference.saveUserSession(user = user)
    }

    override suspend fun deleteUserSession() {
        preference.deleteUserSession()
    }
}