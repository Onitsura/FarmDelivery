package com.onitsura12.farmdel.data.repository

import com.onitsura12.farmdel.data.storage.FirebaseStorage
import com.onitsura12.farmdel.domain.models.User
import com.onitsura12.farmdel.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UserRepositoryImpl@Inject constructor(private val storage: FirebaseStorage) : UserRepository {
    override suspend fun getUser(): Flow<User> {
        return storage.getUserFlow()
    }

    override suspend fun saveUser(newUser: User) {
        TODO("Not yet implemented")
    }
}