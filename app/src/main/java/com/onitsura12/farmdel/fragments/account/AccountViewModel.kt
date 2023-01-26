package com.onitsura12.farmdel.fragments.account


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
        isInWhiteList(UID)
    }



    fun setupAccInfo() {
        setupAccName()
        setupAccEmail()
        setupAccPhone()
        setupAccImage()
        _user.value = USER
    }


    private fun setupAccName() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(
            CHILD_FULLNAME
        )
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER.fullname = snapshot.getValue(String::class.java)
                    _user.value = USER
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }

    private fun setupAccEmail() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(
            FirebaseHelper.CHILD_EMAIL
        )
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER.eMail = snapshot.getValue(String::class.java)
                    _user.value = USER
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }


    private fun setupAccPhone() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(
            FirebaseHelper.CHILD_PHONE
        )
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER.phone = snapshot.getValue(String::class.java).toString()
                    _user.value = USER
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun setupAccImage() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_PHOTO)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER.photoUrl = snapshot.getValue(String::class.java).toString()
                    _user.value = USER
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
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
