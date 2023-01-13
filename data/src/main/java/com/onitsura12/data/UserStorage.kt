package com.onitsura12.data

import com.onitsura12.data.models.StorageUserModel

interface UserStorage {

    fun saveUser(user: StorageUserModel)

    fun getUser(): StorageUserModel

}