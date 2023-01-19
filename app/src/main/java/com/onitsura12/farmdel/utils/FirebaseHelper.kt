package com.onitsura12.farmdel.utils

import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.onitsura12.domain.models.User
import com.onitsura12.farmdel.R
import java.sql.Types.REF
import java.util.*


class FirebaseHelper {


    companion object {

        lateinit var AUTH: FirebaseAuth
        lateinit var UID: String
        lateinit var REF_DATABASE_ROOT: DatabaseReference
        lateinit var USER: User

        const val NODE_WHITELIST = "whitelist"
        const val NODE_SUPPLIES = "supplies"
        const val NODE_USERS = "users"
        const val CHILD_ID = "id"
        const val CHILD_PHONE = "phone"
        const val CHILD_FULLNAME = "fullname"
        const val CHILD_EMAIL = "e-mail"
        const val CHILD_PHOTO = "photo"
        const val CHILD_ADDRESS = "address"
        const val CHILD_CART = "cart"
        const val CHILD_CART_TITLE = "title"
        const val CHILD_CART_COST = "cost"
        const val CHILD_CART_COUNT = "count"
        const val CHILD_CART_WEIGHT = "weight"
        const val CHILD_CART_IMAGE_PATH = "image_path"
        const val CHILD_CART_DELIVERY_DATE = "delivery_date"



        fun initFirebase() {
            AUTH = FirebaseAuth.getInstance()
            REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
            USER = User()
            UID = AUTH.currentUser?.uid.toString()

        }


         fun initUser() {
            val uid = AUTH.currentUser?.uid.toString()
            val dataMap = mutableMapOf<String, Any?>()
            dataMap[CHILD_ID] = uid
            dataMap[CHILD_FULLNAME] = AUTH.currentUser?.displayName
            dataMap[CHILD_EMAIL] = AUTH.currentUser?.email

            REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataMap)
                .addOnCompleteListener {
                    if (it.isComplete) {
                        USER.name = AUTH.currentUser?.displayName
                        USER.eMail = AUTH.currentUser?.email

                    }
                }
        }






    }
}