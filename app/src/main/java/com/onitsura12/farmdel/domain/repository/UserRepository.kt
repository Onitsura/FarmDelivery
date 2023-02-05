package com.onitsura12.farmdel.domain.repository


import com.onitsura12.farmdel.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUser(): Flow<User>

    suspend fun saveUser(newUser: User)

}