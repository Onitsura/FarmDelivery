package com.onitsura12.data

import com.onitsura12.data.models.StorageUserModel
import kotlinx.coroutines.flow.Flow

interface UserStorage {

    suspend fun saveUser(user: StorageUserModel)

    suspend fun getUser(uid: String)

}