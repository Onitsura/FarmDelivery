package com.onitsura12.farmdel.data.datasources

import com.onitsura12.farmdel.domain.models.User
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.UID

class FirebaseDataSource {

    suspend fun getUser(): User {
        return REF_DATABASE_ROOT.child(NODE_USERS).child(UID).get().result.getValue(User::class
            .java)!!
    }

}