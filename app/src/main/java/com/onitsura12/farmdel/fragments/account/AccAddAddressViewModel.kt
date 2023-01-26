package com.onitsura12.farmdel.fragments.account


import androidx.lifecycle.ViewModel
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_ADDRESS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.domain.models.Address
import kotlin.random.Random


class AccAddAddressViewModel : ViewModel() {

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

    fun createNewAddress(
        city: String?,
        street: String?,
        house: String?,
        entrance: String?,
        floor: String?,
        flat: String?
    ): Address {
        return Address(
            city = city,
            street = street,
            house = house,
            entrance = entrance,
            floor = floor,
            flat = flat,
            id = getId().toString(),
            primary = false
        )
    }
}