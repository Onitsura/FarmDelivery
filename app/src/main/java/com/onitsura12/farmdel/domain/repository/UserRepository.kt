package com.onitsura12.farmdel.domain.repository


import com.onitsura12.farmdel.domain.models.User

interface UserRepository {

    fun getUser(): User

    fun saveUser(newUser: User)

}