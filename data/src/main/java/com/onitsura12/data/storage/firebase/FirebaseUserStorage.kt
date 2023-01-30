package com.onitsura12.data.storage.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.Mapper
import com.onitsura12.data.UserStorage
import com.onitsura12.data.models.StorageUserModel
import com.onitsura12.data.storage.firebase.utils.AUTH
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_ADDRESS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_EMAIL
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_FULLNAME
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_ID
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_PHONE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_PHOTO
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.USER

class FirebaseUserStorage : UserStorage {


    override suspend fun saveUser(user: StorageUserModel) {
        val uid = AUTH.currentUser?.uid
        val dataMap = mutableMapOf<String, Any?>()
        dataMap[CHILD_ID] = uid
        dataMap[CHILD_FULLNAME] = user.name
        dataMap[CHILD_EMAIL] = user.eMail
        dataMap[CHILD_ADDRESS] = user.address
        dataMap[CHILD_CART] = user.cart
        dataMap[CHILD_PHONE] = user.phone
        dataMap[CHILD_PHOTO] = user.photoUrl

        if (uid != null) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataMap)
                .addOnCompleteListener {
                    if (it.isComplete) {
                        Log.i("saveuserstorage", user.name.toString())
                    }
                }
        }
    }

    override suspend fun getUser(uid: String) {
        var user = StorageUserModel()
        if (uid.isNotEmpty() && uid.isNotBlank()) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        user = snapshot.getValue(StorageUserModel::class.java)!!
                        USER = Mapper.userToDomain(snapshot.getValue(StorageUserModel::class.java)!!)
                        USER.fullname = "ИЛья"

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

        }

    }
}



















