package com.onitsura12.farmdel.fragments.account


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.AUTH
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_FULLNAME
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.USER
import com.onitsura12.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    val userName: MutableLiveData<String?> = MutableLiveData()
    private val _root: MutableLiveData<Boolean> = MutableLiveData()
    val root: LiveData<Boolean> = _root


    init {
        UID = AUTH.currentUser?.uid.toString()
        isInWhiteList(UID)
        viewModelScope.launch {
        }
        USER.photoUrl = AUTH.currentUser?.photoUrl.toString()
        setupAccInfo()
    }

    private fun setupAccInfo() {
        setupAccName()
        setupAccEmail()
        setupAccPhone()

    }


    private fun setupAccName() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FULLNAME)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        USER.fullname = "Пользователь"
                        userName.value = USER.fullname

                    } else {
                        USER.fullname = snapshot.value.toString()
                        userName.value = USER.fullname
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
