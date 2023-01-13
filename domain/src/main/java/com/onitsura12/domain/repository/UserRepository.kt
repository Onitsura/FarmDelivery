package com.onitsura12.domain.repository

import com.onitsura12.domain.models.User

interface UserRepository {

    fun saveUser(user: User)

    fun getUser(): User

}