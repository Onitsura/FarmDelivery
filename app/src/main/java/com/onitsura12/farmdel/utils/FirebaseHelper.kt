package com.onitsura12.farmdel.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.onitsura12.domain.models.User




class FirebaseHelper {


    companion object {
        lateinit var AUTH: FirebaseAuth
        lateinit var UID: String
        lateinit var REF_DATABASE_ROOT: DatabaseReference
        lateinit var USER: User

        const val NODE_USERS = "users"
        const val CHILD_ID = "id"
        const val CHILD_PHONE = "phone"
        const val CHILD_FULLNAME = "fullname"
        const val CHILD_EMAIL = "e-mail"
        const val CHILD_PHOTO = "photo"

        fun initFirebase() {
            AUTH = FirebaseAuth.getInstance()
            REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
            USER = User()
            UID = AUTH.currentUser?.uid.toString()
        }
    }
}