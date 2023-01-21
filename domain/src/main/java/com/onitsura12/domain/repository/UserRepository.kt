package com.onitsura12.domain.repository

import com.onitsura12.domain.models.User
import kotlinx.coroutines.flow.Flow


interface UserRepository {

    suspend fun saveUser(user: User)

    suspend fun getUser(uid: String)

}