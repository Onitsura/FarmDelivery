package com.onitsura12.data

import com.onitsura12.data.storage.SharedPrefsUserStorage
import com.onitsura12.domain.models.User
import com.onitsura12.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(userStorage: UserStorage) :
UserRepository {

    private val storage = userStorage

    override fun saveUser(user: User) {
        storage.saveUser(user = Mapper.userToData(user = user))
    }

    override fun getUser(): User {
        return Mapper.userToDomain(storage.getUser())
    }


}