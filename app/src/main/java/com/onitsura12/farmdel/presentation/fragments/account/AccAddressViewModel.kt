package com.onitsura12.farmdel.presentation.fragments.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.farmdel.domain.models.Address
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_ADDRESS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_ADDRESS_PRIMARY
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.UID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccAddressViewModel @Inject constructor() : ViewModel() {

    private val _addressList: MutableLiveData<ArrayList<Address>> = MutableLiveData()
    val addressList: LiveData<ArrayList<Address>> = _addressList


    init {
        initAddressList()
    }

    private fun initAddressList() {
        val list = arrayListOf<Address>()
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_ADDRESS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    _addressList.value = list
                    for (addressSnapshot in snapshot.children) {
                        val address = addressSnapshot.getValue(Address::class.java)
                        list.add(address!!)

                    }
                    _addressList.value = list

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun markAddress(address: Address) {
        val newValue = true
        address.primary = newValue
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_ADDRESS)
            .child(address.id)
            .child(CHILD_ADDRESS_PRIMARY).setValue(newValue)
            .addOnCompleteListener {
                updateAddresses(address = address)
            }
    }

    private fun updateAddresses(address: Address) {
        val list: ArrayList<Address> = _addressList.value!!
        for (i in list.indices) {
            if (list[i].id != address.id) {
                REF_DATABASE_ROOT.child(NODE_USERS)
                    .child(UID)
                    .child(CHILD_ADDRESS)
                    .child(list[i].id)
                    .child(CHILD_ADDRESS_PRIMARY).setValue(false)
            }
        }
    }

    fun removeAddress(address: Address) {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_ADDRESS)
            .child(address.id)
            .removeValue()
            .addOnCompleteListener {
                updateAddresses(address = address)
            }
    }

    fun editAddress(address: Address) {

    }
}



