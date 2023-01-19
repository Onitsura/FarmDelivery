package com.onitsura12.farmdel.fragments.account


import androidx.lifecycle.ViewModel
import com.onitsura12.domain.models.Address
import com.onitsura12.farmdel.utils.FirebaseHelper
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_ADDRESS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.UID
import kotlin.random.Random


class AccAddAddressViewModel : ViewModel() {

    fun getId(): Int{
        return Random.nextInt(35000)
    }

    fun createNewAddress(newAddress: Address){
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_ADDRESS)
            .child(newAddress.id)
            .setValue(newAddress)
    }

}