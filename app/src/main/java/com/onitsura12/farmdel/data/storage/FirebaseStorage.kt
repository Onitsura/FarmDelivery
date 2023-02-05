package com.onitsura12.farmdel.data.storage

import com.onitsura12.farmdel.data.datasources.FirebaseDataSource
import com.onitsura12.farmdel.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class FirebaseStorage(private val firebaseDataSource: FirebaseDataSource) {

    suspend fun getUserFlow(): Flow<User>{
       return firebaseDataSource.getUser().asFlow()
    }
}

private fun User.asFlow(): Flow<User> {
    val list = mutableListOf(this)
    return flow {
        emitAll(list.asFlow())
    }.flowOn(Dispatchers.IO)
}
