package com.onitsura12.farmdel.presentation.fragments.account


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.farmdel.domain.models.Address
import com.onitsura12.farmdel.utils.FirebaseHelper
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_ADDRESS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.UID
import kotlin.random.Random


class AccAddAddressViewModel : ViewModel() {

    private val _addressList: MutableLiveData<ArrayList<Address>> = MutableLiveData()


    init {
        initAddressList()
    }

    private fun getId(): Int {
        return Random.nextInt(35000)
    }

    fun addNewAddress(newAddress: Address) {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_ADDRESS)
            .child(newAddress.id)
            .setValue(newAddress)
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

    fun createNewAddress(
        city: String?,
        street: String?,
        house: String?,
        entrance: String?,
        floor: String?,
        flat: String?
    ): Address {

        val newAddress =  Address(
            city = city,
            street = street,
            house = house,
            entrance = entrance,
            floor = floor,
            flat = flat,
            id = getId().toString(),
            primary = true
        )

        updateAddresses(newAddress)
        return newAddress
    }

    private fun updateAddresses(address: Address) {
        val list: ArrayList<Address> = _addressList.value!!
        for (i in list.indices) {
            if (list[i].id != address.id) {
                REF_DATABASE_ROOT.child(NODE_USERS)
                    .child(UID)
                    .child(CHILD_ADDRESS)
                    .child(list[i].id)
                    .child(FirebaseHelper.CHILD_ADDRESS_PRIMARY).setValue(false)
            }
        }
    }
}