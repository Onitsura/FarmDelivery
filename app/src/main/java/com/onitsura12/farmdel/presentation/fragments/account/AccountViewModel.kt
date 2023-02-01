package com.onitsura12.farmdel.presentation.fragments.account


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.farmdel.domain.models.User
import com.onitsura12.farmdel.utils.FirebaseHelper
import com.onitsura12.farmdel.utils.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.UID
import com.onitsura12.farmdel.utils.USER
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() :
    ViewModel() {

    private val _root: MutableLiveData<Boolean> = MutableLiveData()
    val root: LiveData<Boolean> = _root


    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    init {
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
