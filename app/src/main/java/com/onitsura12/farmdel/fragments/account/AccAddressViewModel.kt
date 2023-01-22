package com.onitsura12.farmdel.fragments.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_ADDRESS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_ADDRESS_PRIMARY
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.domain.models.Address
import com.onitsura12.domain.models.ShopItem
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
                    for (addressSnapshot in snapshot.children) {
                        val address = addressSnapshot.getValue(Address::class.java)
                        list.add(address!!)
                    }
                    _addressList.value = list
                    Log.i("checked2", _addressList.value.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun markAddress(address: Address) {
        val newValue = !address.primary
        address.primary = newValue



        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_ADDRESS)
            .child(address.id)
            .child(CHILD_ADDRESS_PRIMARY).setValue(newValue)
            .addOnCompleteListener {

                if (newValue) {
                    for (i in _addressList.value!!.indices) {
                        if (_addressList.value!![i].id != address.id) {
                            val dataMap = mutableMapOf<String, Any?>()
                            dataMap[CHILD_ADDRESS_PRIMARY] = false
                            REF_DATABASE_ROOT.child(NODE_USERS)
                                .child(UID)
                                .child(CHILD_ADDRESS)
                                .child(_addressList.value!![i].id)
                                .updateChildren(dataMap)
                        }
                    }
                    Log.i("checked", _addressList.value.toString())
                }
            }
    }
}



