package com.onitsura12.data.repository

import com.onitsura12.data.Mapper
import com.onitsura12.data.UserStorage
import com.onitsura12.domain.models.User
import com.onitsura12.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(userStorage: UserStorage) :
UserRepository {

    private val storage = userStorage

    override suspend fun saveUser(user: User) {
        storage.saveUser(user = Mapper.userToData(user = user))
    }

    override suspend fun getUser(uid: String){
        storage.getUser(uid = uid)
    }


}

private fun User.asFlow(): Flow<User> {
    val list = mutableListOf(this)
    return flow {
        emitAll(list.asFlow())
    }.flowOn(Dispatchers.IO)
}
