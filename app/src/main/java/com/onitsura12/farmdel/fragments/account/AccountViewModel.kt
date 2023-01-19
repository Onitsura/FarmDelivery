package com.onitsura12.farmdel.fragments.account

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.farmdel.utils.FirebaseHelper
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.AUTH
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_FULLNAME
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.UID
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.USER

class AccountViewModel : ViewModel() {
    val userName: MutableLiveData<String?> = MutableLiveData()


    init {
        UID = AUTH.currentUser?.uid.toString()
        USER.photoUrl = AUTH.currentUser?.photoUrl.toString()
        setupAccInfo()


    }

    private fun setupAccInfo() {
        setupAccName()
        setupAccEmail()
        setupAccPhone()
    }

    private fun setupAccName() {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_FULLNAME)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        USER.name = "Пользователь"
                        userName.value = USER.name

                    } else {
                        USER.name = snapshot.value.toString()
                        userName.value = USER.name
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }

    private fun setupAccEmail() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(FirebaseHelper.CHILD_EMAIL).get()
            .addOnCompleteListener {
                USER.eMail = it.result.value.toString()
            }

    }


    private fun setupAccPhone() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(FirebaseHelper.CHILD_PHONE).get()
            .addOnCompleteListener {
                USER.phone = it.result.value.toString()

            }
    }



    }
