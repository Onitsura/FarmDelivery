package com.onitsura12.farmdel.fragments.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.domain.models.Address
import com.onitsura12.farmdel.utils.FirebaseHelper

class ConfirmOrderViewModel : ViewModel() {
    private val _addressList: MutableLiveData<ArrayList<Address>> = MutableLiveData()
    val addressList: LiveData<ArrayList<Address>> = _addressList


    init {
        initAddressList()
    }


    private fun initAddressList() {
        val list = arrayListOf<Address>()
        FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseHelper.NODE_USERS)
            .child(FirebaseHelper.UID)
            .child(FirebaseHelper.CHILD_ADDRESS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (addressSnapshot in snapshot.children) {
                        val address = addressSnapshot.getValue(Address::class.java)
                        list.add(address!!)
                        _addressList.value = list
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}