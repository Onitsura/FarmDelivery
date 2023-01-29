package com.onitsura12.farmdel.fragments.account


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.AUTH
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_FULLNAME
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_PHOTO
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.USER
import com.onitsura12.domain.models.User
import com.onitsura12.domain.repository.UserRepository
import com.onitsura12.farmdel.utils.LoginUtils.Companion.setupAccInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _root: MutableLiveData<Boolean> = MutableLiveData()
    val root: LiveData<Boolean> = _root


    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    init {
        UID = AUTH.currentUser?.uid.toString()
        setupAccInfo()
        _user.value = USER
        isInWhiteList(UID)
    }






    private fun isInWhiteList(id: String) {
        _root.value = false
        REF_DATABASE_ROOT.child(FirebaseHelper.NODE_WHITELIST)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (supplySnapshot in snapshot.children) {
                        val whiteListItem = supplySnapshot.getValue(String::class.java)
                        if (id == whiteListItem) {
                            _root.value = true
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }


}
