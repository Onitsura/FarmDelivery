package com.onitsura12.farmdel.fragments.account

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_EMAIL
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_FULLNAME
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_PHONE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.USER
import com.onitsura12.domain.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccDetailsViewModel @Inject constructor() : ViewModel() {
    private val _isPhoneCorrect: MutableLiveData<Boolean> = MutableLiveData()
    val isPhoneCorrect: LiveData<Boolean> = _isPhoneCorrect
    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user


    init {
        setupAccInfo()
    }

    private fun setupAccInfo() {
        setupAccName()
        setupAccEmail()
        setupAccPhone()
        _user.value = USER
    }


    private fun setupAccName() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FULLNAME)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER.fullname = snapshot.getValue(String::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }

    private fun setupAccEmail() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_EMAIL)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER.eMail = snapshot.getValue(String::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }


    private fun setupAccPhone() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_PHONE)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER.phone = snapshot.getValue(String::class.java).toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


    fun checkPhone(phone: String) {
        _isPhoneCorrect.value = false
        if (phone.length == 10 || phone.length == 11 || phone.length == 12) {
            _isPhoneCorrect.value = true
        }
    }

    fun saveNewName(newName: String, context: Context) {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_FULLNAME)
            .setValue(newName)
            .addOnCompleteListener {
                USER.fullname = newName
                _user.value = USER
                Toast.makeText(context, "Имя сохранено", Toast.LENGTH_SHORT)
                    .show()

            }
    }

    fun saveNewPhone(newPhone: String, context: Context) {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_PHONE)
            .setValue(newPhone)
            .addOnCompleteListener {
                USER.phone = newPhone
                _user.value = USER
                Toast.makeText(context, "Номер сохранён", Toast.LENGTH_SHORT).show()
            }
    }

}